export interface IOpCtrlReq {
    id: number;
    command: string;
}

export class OpCtrlReq implements IOpCtrlReq {
    constructor(public id: number, public command: string) {}
}
