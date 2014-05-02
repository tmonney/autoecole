FROM ubuntu:14.04
MAINTAINER Thierry Monney <thierry.monney@gmail.com>

ENV DEBIAN_FRONTEND noninteractive

RUN echo 'Acquire::http::Proxy "http://172.17.42.1:3142";' > /etc/apt/apt.conf.d/90-apt-cacher.conf
RUN echo 'Acquire::https::Proxy "false";' >> /etc/apt/apt.conf.d/90-apt-cacher.conf

# Ensure UTF-8
RUN apt-get update
RUN locale-gen en_US.UTF-8
ENV LANG       en_US.UTF-8
ENV LC_ALL     en_US.UTF-8

# Fix upstart issue as per https://github.com/dotcloud/docker/issues/1024
RUN dpkg-divert --local --rename --add /sbin/initctl && ln -sf /bin/true /sbin/initctl

RUN apt-get install -y puppet-common wget software-properties-common git
ADD hiera.yaml /etc/puppet/hiera.yaml

