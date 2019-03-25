export interface IEntityRef {
    id?: number;
    name?: string;
}

export class EntityRef implements IEntityRef {
    constructor(public id?: number, public name?: string) {}
}
