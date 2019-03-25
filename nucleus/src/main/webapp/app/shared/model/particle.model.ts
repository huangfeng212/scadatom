import { IEntityRef } from 'app/shared/model/entity-ref.model';

export interface IParticle {
    id?: number;
    name?: string;
    decimalFormat?: string;
    initValue?: string;
    electron?: IEntityRef;
    smmBond?: IEntityRef;
    smsBond?: IEntityRef;
}

export class Particle implements IParticle {
    constructor(
        public id?: number,
        public name?: string,
        public decimalFormat?: string,
        public initValue?: string,
        public electron?: IEntityRef,
        public smmBond?: IEntityRef,
        public smsBond?: IEntityRef
    ) {}
}
