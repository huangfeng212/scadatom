import { IEntityRef } from 'app/shared/model/entity-ref.model';

export const enum RegType {
    Coil = 'Coil',
    InputDiscrete = 'InputDiscrete',
    InputReg = 'InputReg',
    HoldingReg = 'HoldingReg'
}

export const enum ValueType {
    Uint16 = 'Uint16',
    Int16 = 'Int16',
    Fp32 = 'Fp32'
}

export interface ISmmBond {
    id?: number;
    enabled?: boolean;
    regType?: RegType;
    reg?: string;
    valueType?: ValueType;
    exprIn?: string;
    exprOut?: string;
    particle?: IEntityRef;
    smmDevice?: IEntityRef;
}

export class SmmBond implements ISmmBond {
    constructor(
        public id?: number,
        public enabled?: boolean,
        public regType?: RegType,
        public reg?: string,
        public valueType?: ValueType,
        public exprIn?: string,
        public exprOut?: string,
        public particle?: IEntityRef,
        public smmDevice?: IEntityRef
    ) {
        this.enabled = this.enabled || false;
    }
}
