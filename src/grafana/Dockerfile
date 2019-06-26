FROM grafana/grafana:latest

USER grafana

ARG GF_INSTALL_PLUGINS="grafana-worldmap-panel"

RUN if [ ! -z "${GF_INSTALL_PLUGINS}" ]; then \
    OLDIFS=$IFS; \
        IFS=','; \
    for plugin in ${GF_INSTALL_PLUGINS}; do \
        IFS=$OLDIFS; \
        grafana-cli --pluginsDir "$GF_PATHS_PLUGINS" plugins install ${plugin}; \
    done; \
fi

USER root

ADD ./provisioning /etc/grafana/provisioning
ADD ./config.ini /etc/grafana/grafana.ini
ADD ./dashboards /var/lib/grafana/dashboards
