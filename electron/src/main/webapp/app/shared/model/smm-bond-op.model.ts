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

export const enum SmmPollStatus {
    NA = 'NA',
    Normal = 'Normal',
    IOError = 'IOError',
    IllegalFunction = 'IllegalFunction',
    IllegalDataAddress = 'IllegalDataAddress',
    IllegalDataValue = 'IllegalDataValue',
    SlaveDeviceFailure = 'SlaveDeviceFailure',
    Acknowledge = 'Acknowledge',
    SlaveDeviceBusy = 'SlaveDeviceBusy',
    MemoryParityError = 'MemoryParityError',
    UnknownError = 'UnknownError'
}

export interface ISmmBondOp {
    id?: number;
    state?: OpState;
    dt?: Moment;
    pollStatus?: SmmPollStatus;
    readRequest?: string;
    readResponse?: string;
    writeRequest?: string;
    writeResponse?: string;
    writeRequestDt?: Moment;
}

export class SmmBondOp implements ISmmBondOp {
    constructor(
        public id?: number,
        public state?: OpState,
        public dt?: Moment,
        public pollStatus?: SmmPollStatus,
        public readRequest?: string,
        public readResponse?: string,
        public writeRequest?: string,
        public writeResponse?: string,
        public writeRequestDt?: Moment
    ) {}
}
