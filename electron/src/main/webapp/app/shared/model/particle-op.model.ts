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

export interface IParticleOp {
    id?: number;
    state?: OpState;
    dt?: Moment;
    value?: string;
    writtenBy?: string;
    writtenDt?: Moment;
}

export class ParticleOp implements IParticleOp {
    constructor(
        public id?: number,
        public state?: OpState,
        public dt?: Moment,
        public value?: string,
        public writtenBy?: string,
        public writtenDt?: Moment
    ) {}
}
