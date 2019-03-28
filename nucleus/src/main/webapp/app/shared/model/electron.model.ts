import {IEntityRef} from 'app/shared/model/entity-ref.model';

export interface IElectron {
  id?: number;
  name?: string;
  enabled?: boolean;
  particles?: IEntityRef[];
  smmCharger?: IEntityRef;
  smsCharger?: IEntityRef;
}

export class Electron implements IElectron {
  constructor(
      public id?: number,
      public name?: string,
      public enabled?: boolean,
      public particles?: IEntityRef[],
      public smmCharger?: IEntityRef,
      public smsCharger?: IEntityRef
  ) {
    this.enabled = this.enabled || false;
  }
}
