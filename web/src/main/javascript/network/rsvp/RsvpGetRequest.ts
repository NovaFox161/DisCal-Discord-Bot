import {AsyncTask} from "@/objects/task/AsyncTask";
import {TaskCallback} from "@/objects/task/TaskCallback";
import {NetworkCallStatus} from "@/objects/network/NetworkCallStatus";
import {TaskType} from "@/enums/TaskType";
import jqXHR = JQuery.jqXHR;

export class RsvpGetRequest implements AsyncTask {
	private readonly guildId: string;
	private readonly eventId: string;

	readonly callback: TaskCallback;

	apiKey: string = "";
	apiUrl: string = "";


	constructor(guildId: string, eventId: string, callback: TaskCallback) {
		this.guildId = guildId;
		this.eventId = eventId;
		this.callback = callback;
	}

	provideApiDetails(apiKey: string, apiUrl: string): void {
		this.apiKey = apiKey;
		this.apiUrl = apiUrl;
	}

    execute(): void {
        let bodyRaw = {
            "guild_id": this.guildId,
            "event_id": this.eventId
        };

        $.ajax({
            url: this.apiUrl + "/v2/rsvp/get",
            headers: {
                "Content-Type": "application/json",
                "Authorization": this.apiKey
            },
            method: "POST",
            dataType: "json",
            data: JSON.stringify(bodyRaw),
            success: function (json: any) {
                let status = new NetworkCallStatus(true, TaskType.RSVP_GET);
                status.code = 200;
                status.body = json;
                status.message = "Success";

                this.onComplete(status);

            }.bind(this),
            error: function (jqXHR: jqXHR) {

                let status = new NetworkCallStatus(false, TaskType.RSVP_GET);
                status.code = jqXHR.status;
                status.body = jqXHR.responseJSON;
                status.message = jqXHR.responseJSON.message;

                this.onComplete(status);
            }.bind(this)
        });
    }

    onComplete(status: NetworkCallStatus): void {
        this.callback.onCallback(status);
    }
}