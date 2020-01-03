FROM nginx

COPY site.conf /etc/nginx/conf.d/default.conf
WORKDIR /data/www/
COPY . .
RUN apt-get -y update && apt install -y curl
RUN curl -sL https://deb.nodesource.com/setup_10.x | bash -
RUN apt install -y nodejs
RUN npm install