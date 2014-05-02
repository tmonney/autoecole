#!/bin/bash

#set -e

c_cyan=$(tput setaf 6)
c_red=$(tput setaf 1)
c_green=$(tput setaf 2)
c_yellow=$(tput setaf 3)
c_sgr0=$(tput sgr0)

function print_with_color() {
	local color=$1
	local msg=$2
	echo -ne "$color$msg$c_sgr0"
}

function println_with_color() {
	print_with_color "$@"
	echo
}

function run_task() {
	local msg="$1"
	local cmd="$2"

	println_with_color "$c_cyan" "$msg"
	$cmd
	code=$?
	if [[ $code -eq 0 ]]; then
		println_with_color "$c_green" "Done"
		return $code
	else
		println_with_color "$c_red" "Command failed!"
		exit $code
	fi
}

function setup_proxy() {
	# Install a proxy so that we can reuse downloaded packages in Docker containers
	sudo apt-get -qq install -y apt-cacher-ng

	PROXY="http://localhost:3142"

	sudo tee /etc/apt/apt.conf.d/90-apt-cacher.conf >/dev/null <<APT_CACHER_EOF
Acquire::http::Proxy "$PROXY";
Acquire::https::Proxy "false";
APT_CACHER_EOF
}

function install_base_packages() {
	sudo apt-get -qq install -y docker.io git wget puppet-common software-properties-common
}

function setup_puppet() {
	# Fix annoying puppet warning
	sudo tee /etc/puppet/hiera.yaml >/dev/null <<HIERA_EOF
---
:backends: yaml
:yaml:
  :datadir: /var/lib/hiera
:hierarchy: common
:logger: console
HIERA_EOF
}

function install_oracle_java7() {
	if ! sudo puppet module list | grep softek-java7 >/dev/null; then
		sudo puppet module install softek-java7 >/dev/null
	fi
	sudo puppet apply -e "include java7" >/dev/null
}

function install_scala() {
	sudo http_proxy="$PROXY" wget -q http://downloads.typesafe.com/scala/2.11.0/scala-2.11.0.deb
	sudo dpkg -i -E scala-2.11.0.deb >/dev/null
	sudo rm scala-2.11.0.deb
}

function install_sbt() {
	sudo http_proxy="$PROXY" wget -q http://dl.bintray.com/sbt/debian/sbt-0.13.2.deb
	sudo dpkg -i -E sbt-0.13.2.deb >/dev/null
	sudo rm sbt-0.13.2.deb
}

function install_nodejs() {
	if ! sudo puppet module list | grep puppetlabs-nodejs >/dev/null; then
		sudo puppet module install puppetlabs-nodejs >/dev/null
	fi

	nodejs_manifest=$(mktemp)
	cat <<NODEJS_PP_EOF > $nodejs_manifest
include nodejs
package { 'grunt-cli':
	ensure => present,
	provider => 'npm',
}
package { 'karma':
	ensure => present,
	provider => 'npm',
}
package { 'bower':
	ensure => present,
	provider => 'npm',
}
NODEJS_PP_EOF

	sudo puppet apply $nodejs_manifest #>/dev/null
	rm $nodejs_manifest
}

function bootstrap_modules() {
	for f in $( find . -mindepth 2 -maxdepth 2 -executable -name 'bootstrap.sh'); do
		script=$(basename $f)
		dir=$(dirname $f)
		echo "Running script $script in $dir"
		cd $dir
			./$script
		cd - >/dev/null
	done
}

function build_docker_images() {
	docker build -q -t autoecole/base docker/base >/dev/null
	docker build -q -t autoecole/scala docker/scala >/dev/null
	docker build -q -t autoecole/nginx docker/nginx >/dev/null
	docker build -q -t autoecole/postgresql docker/postgresql >/dev/null
	docker build -q -t autoecole/api docker/api >/dev/null
}

run_task "Set up package cache proxy" "setup_proxy"
run_task "Install base packages" "install_base_packages"
run_task "Set up Puppet" "setup_puppet"
run_task "Install Oracle JDK 7" "install_oracle_java7"
run_task "Install Scala" "install_scala"
run_task "Install SBT" "install_sbt"
run_task "Install Node.js" "install_nodejs"
run_task "Bootstrap project child modules" "bootstrap_modules"
run_task "Build Docker images" "build_docker_images"
