FROM autoecole/base
MAINTAINER Thierry Monney <thierry.monney@gmail.com>

RUN puppet module install jfryman-nginx
ADD provision.pp /root/
RUN FACTER_fqdn=www.autoecole.ch puppet apply /root/provision.pp
RUN echo "daemon off;" >> /etc/nginx/nginx.conf

VOLUME ["/var/log/nginx", "/etc/nginx"]

EXPOSE 80

ADD start.sh /root/start.sh
RUN chmod +x /root/start.sh
CMD ["/root/start.sh"]