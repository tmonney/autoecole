FROM autoecole/base
MAINTAINER Thierry Monney <thierry.monney@gmail.com>

RUN puppet module install softek-java7
RUN FACTER_fqdn=www.autoecole.ch puppet apply -e "include java7"

# install scala
RUN http_proxy="http://172.17.42.1:3142" wget -q http://downloads.typesafe.com/scala/2.11.0/scala-2.11.0.deb
RUN dpkg -i scala-2.11.0.deb

# install sbt
RUN http_proxy="http://172.17.42.1:3142" wget -q http://dl.bintray.com/sbt/debian/sbt-0.13.2.deb
RUN dpkg -i sbt-0.13.2.deb
