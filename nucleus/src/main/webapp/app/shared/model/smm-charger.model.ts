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

export interface ISmmCharger {
    id?: number;
    enabled?: boolean;
    port?: string;
    baud?: number;
    databit?: number;
    parity?: Parity;
    stopbit?: Stopbit;
    timeout?: number;
    retry?: number;
    transDelay?: number;
    batchDelay?: number;
    electron?: IEntityRef;
    smmDevices?: IEntityRef[];
}

export class SmmCharger implements ISmmCharger {
    constructor(
        public id?: number,
        public enabled?: boolean,
        public port?: string,
        public baud?: number,
        public databit?: number,
        public parity?: Parity,
        public stopbit?: Stopbit,
        public timeout?: number,
        public retry?: number,
        public transDelay?: number,
        public batchDelay?: number,
        public electron?: IEntityRef,
        public smmDevices?: IEntityRef[]
    ) {
        this.enabled = this.enabled || false;
    }
}
