import { Component, OnInit } from "@angular/core";

import { Subscription } from "rxjs";

import { WebsocketService } from "../_services/websocket.service";
import { PluginApiService } from "./api.service";

import { SystemMonitorService, SystemMonitorMessage } from "./sysmonitor.service";

const PLUGIN_NAME = "System Monitor Plugin";

interface MonitorAttrib {
    value: string;
    unit: string;
}

interface MonitorAttribs {
    [name: string]: MonitorAttrib;
}

@Component({
    selector: "ui-sysmonitor-plugin",
    templateUrl: "./sysmonitor.component.html",
    providers: [WebsocketService, SystemMonitorService]
})
export class SystemMonitorComponent implements OnInit {

    socket: Subscription;

    entries: MonitorAttribs = {};

    constructor(private $api: PluginApiService, private $svc: SystemMonitorService) {
    }

    ngOnInit() {
        this.$api.isPluginEnabled(PLUGIN_NAME).subscribe((enabled: boolean) => {
            if (enabled) {
                this.socket = this.$svc.events.subscribe((msg: SystemMonitorMessage) => {
                    this.handleMessage(msg);
                });
            }
        });

    }

    private handleMessage(msg: SystemMonitorMessage) {
        msg.attribs.forEach(a => {
            if (!this.entries[a.name]) {
                this.entries[a.name] = <MonitorAttrib>{
                    value: a.value,
                    unit: a.unit
                };
            } else {
                let val: any = a.value.indexOf(".") !== -1 ? parseFloat(a.value) : parseInt(a.value, 10);
                val = isNaN(val) ? a.value : val;

                this.entries[a.name].value = ["C", "F"].indexOf(a.unit) && val === 0 ? this.entries[a.name].value : val;
            }
        });
    }

    gaugeLabel(name: string, attrib: MonitorAttrib) {
        if (attrib) {
            if (["C", "F"].indexOf(attrib.unit) !== -1) {
                return (value: number): string => {
                    return `${Math.round(value)} °` + attrib.unit;
                };
            } else if (["memory", "swap"].indexOf(name) !== -1) {
                return (value: number): string => {
                    if (value >= (1024 * 1024 * 1024)) {
                        return `${Math.round(value / (1024 * 1024 * 1024))} G`;
                    } else if (value >= (1024 * 1024)) {
                        return `${Math.round(value / (1024 * 1024))} M`;
                    }

                    return `${Math.round(value / 1024)} K`;
                };
            } else {
                return (value: number): string => {
                    return `${Math.round(value)} ` + attrib.unit;
                };
            }
        }

        return null;
    }

}
