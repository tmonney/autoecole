class { 'nginx':
	confd_purge => true,
	vhost_purge => true,
	service_ensure => stopped,
}

# Frontend web application
nginx::resource::vhost { 'www.autoecole.ch':
  www_root 		=> '/var/www/autoecole.ch',
}

# Backend services
nginx::resource::vhost { 'api.autoecole.ch':
  proxy 		=> 'http://api',
}

# API server(s)
nginx::resource::upstream { 'api':
  members => [
    '##API_HOST##',
  ],
}

