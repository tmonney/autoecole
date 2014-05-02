class { 'postgresql::server': 
	version 			=> "9.3",
	postgres_password 	=> "postgres",
	listen_addresses	=> "*",
}

postgresql::server::pg_hba_rule { 'Allow application network to access app database':
  type => 'host',
  database => 'autoecole',
  user => 'autoecole',
  address => '0.0.0.0/0',
  auth_method => 'md5',
}

postgresql::server::db { 'autoecole':
  user     => 'autoecole',
  password => postgresql_password('autoecole', 'autoecole'),
}