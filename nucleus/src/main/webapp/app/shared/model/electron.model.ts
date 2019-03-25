import { IEntityRef } from 'app/shared/model/entity-ref.model';

export interface IElectron {
    id?: number;
    name?: string;
    particles?: IEntityRef[];
    smmCharger?: IEntityRef;
    smsCharger?: IEntityRef;
}

export class Electron implements IElectron {
    constructor(
        public id?: number,
        public name?: string,
        public particles?: IEntityRef[],
        public smmCharger?: IEntityRef,
        public smsCharger?: IEntityRef
    ) {}
}
