import { IEntityRef } from 'app/shared/model/entity-ref.model';

export const enum Parity {
    None = 'None',
    Odd = 'Odd',
    Even = 'Even',
    Mark = 'Mark',
    Space = 'Space'
}

export const enum Stopbit {
    NA = 'NA',
    One = 'One',
    OnePointFive = 'OnePointFive',
    Two = 'Two'
}

export interface ISmsCharger {
    id?: number;
    enabled?: boolean;
    port?: string;
    baud?: number;
    databit?: number;
    parity?: Parity;
    stopbit?: Stopbit;
    respDelay?: number;
    electron?: IEntityRef;
    smsDevices?: IEntityRef[];
}

export class SmsCharger implements ISmsCharger {
    constructor(
        public id?: number,
        public enabled?: boolean,
        public port?: string,
        public baud?: number,
        public databit?: number,
        public parity?: Parity,
        public stopbit?: Stopbit,
        public respDelay?: number,
        public electron?: IEntityRef,
        public smsDevices?: IEntityRef[]
    ) {
        this.enabled = this.enabled || false;
    }
}
