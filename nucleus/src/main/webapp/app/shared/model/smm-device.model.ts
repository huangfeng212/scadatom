import { IEntityRef } from 'app/shared/model/entity-ref.model';

export interface ISmmDevice {
    id?: number;
    enabled?: boolean;
    name?: string;
    address?: string;
    smmBonds?: IEntityRef[];
    smmCharger?: IEntityRef;
}

export class SmmDevice implements ISmmDevice {
    constructor(
        public id?: number,
        public enabled?: boolean,
        public name?: string,
        public address?: string,
        public smmBonds?: IEntityRef[],
        public smmCharger?: IEntityRef
    ) {
        this.enabled = this.enabled || false;
    }
}
