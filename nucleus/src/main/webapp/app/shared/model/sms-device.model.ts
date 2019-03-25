import { IEntityRef } from 'app/shared/model/entity-ref.model';

export interface ISmsDevice {
    id?: number;
    enabled?: boolean;
    name?: string;
    address?: string;
    smsBonds?: IEntityRef[];
    smsCharger?: IEntityRef;
}

export class SmsDevice implements ISmsDevice {
    constructor(
        public id?: number,
        public enabled?: boolean,
        public name?: string,
        public address?: string,
        public smsBonds?: IEntityRef[],
        public smsChargerId?: IEntityRef
    ) {
        this.enabled = this.enabled || false;
    }
}
