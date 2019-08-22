import {
    Component, OnInit, Directive, TemplateRef, ContentChildren,
    QueryList, AfterContentChecked, OnDestroy, Output, EventEmitter
} from "@angular/core";

import { Subscription } from "rxjs";

import { Attrib } from "./definitions";
import { SystemMonitorService, SystemMonitorMessage } from "./sysmonitor.service";

export interface MonitorAttribs {
    [name: string]: Attrib;
}

@Directive({ selector: "ng-template[sysMonContent]" })
export class SystemMonitorContentDirective {

    @Output()
    entries: EventEmitter<MonitorAttribs> = new EventEmitter();

    constructor(public templateRef: TemplateRef<any>) { }
}

@Component({
    selector: "ui-sysmonitor-plugin",
    templateUrl: "./sysmonitor.component.html"
})
export class SystemMonitorComponent implements OnInit, OnDestroy, AfterContentChecked {

    socket: Subscription;

    entries: MonitorAttribs = {};

    contentTpl: SystemMonitorContentDirective | null;

    @ContentChildren(SystemMonitorContentDirective, { descendants: false })
    contentTpls: QueryList<SystemMonitorContentDirective>;

    constructor(private $svc: SystemMonitorService) {
    }

    ngOnInit() {
        this.$svc.informIsEnabled().subscribe(enabled => {
            if (enabled && !this.socket) {
                this.socket = this.$svc.getSubject().subscribe((msg: SystemMonitorMessage) => {
                    this.handleMessage(msg);
                });
            }
        });
    }

    ngOnDestroy() {
        if (this.socket) {
            this.socket.unsubscribe();
        }
    }

    ngAfterContentChecked() {
        this.contentTpl = this.contentTpls.first;
    }

    private handleMessage(msg: SystemMonitorMessage) {
        if (msg && msg.attribs) {
            msg.attribs.forEach(a => {
                if (!this.entries[a.name]) {
                    this.entries[a.name] = <Attrib>{
                        value: a.value,
                        unit: a.unit
                    };
                } else {
                    let val: any = a.value.indexOf(".") !== -1 ? parseFloat(a.value) : parseInt(a.value, 10);
                    val = isNaN(val) ? a.value : val;

                    this.entries[a.name].value = ["C", "F"].indexOf(a.unit) !== -1 && val === 0 ? this.entries[a.name].value : val;
                }
            });

            if (this.contentTpl) {
                this.contentTpl.entries.emit(this.entries);
            }
        }
    }

}
