import { Moment } from 'moment';

export const enum OpState {
    Uninitialized = 'Uninitialized',
    Initialized = 'Initialized',
    Undefined = 'Undefined',
    Disabled = 'Disabled',
    Stopped = 'Stopped',
    Started = 'Started',
    Aborted = 'Aborted'
}

export interface ISmmChargerOp {
    id?: number;
    state?: OpState;
    dt?: Moment;
}

export class SmmChargerOp implements ISmmChargerOp {
    constructor(public id?: number, public state?: OpState, public dt?: Moment) {}
}
