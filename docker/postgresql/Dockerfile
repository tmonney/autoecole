FROM autoecole/base
MAINTAINER Thierry Monney <thierry.monney@gmail.com>

RUN puppet module install puppetlabs/postgresql
ADD provision.pp /root/
RUN FACTER_fqdn=db.autoecole.ch puppet apply /root/provision.pp

VOLUME ["/var/log/postgresql", "/etc/postgresql", "/var/lib/postgresql"]

USER postgres

EXPOSE 5432

CMD ["/usr/lib/postgresql/9.3/bin/postgres", "-D", "/var/lib/postgresql/9.3/main", "-c", "config_file=/etc/postgresql/9.3/main/postgresql.conf"]