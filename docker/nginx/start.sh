#!/bin/bash

API_HOST="${API_PORT_8080_TCP_ADDR}:${API_PORT_8080_TCP_PORT}"

sed -i -e "s/##API_HOST##/$API_HOST/" /etc/nginx/conf.d/api-upstream.conf
nginx