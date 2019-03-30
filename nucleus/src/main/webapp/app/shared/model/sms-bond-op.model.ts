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

export interface ISmsBondOp {
    id?: number;
    state?: OpState;
    dt?: Moment;
    bytes?: string;
    writtenDt?: Moment;
    writtenBytes?: string;
}

export class SmsBondOp implements ISmsBondOp {
    constructor(
        public id?: number,
        public state?: OpState,
        public dt?: Moment,
        public bytes?: string,
        public writtenDt?: Moment,
        public writtenBytes?: string
    ) {}
}
