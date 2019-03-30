import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {IParticle} from 'app/shared/model/particle.model';
import {IParticleOp} from 'app/shared/model/particle-op.model';
import {ParticleService} from 'app/entities/particle/particle.service';
import {OpCtrlReq} from 'app/shared/model/operation.model';
import {Observable} from 'rxjs';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';

@Component({
  selector: 'jhi-particle-detail',
  templateUrl: './particle-detail.component.html'
})
export class ParticleDetailComponent implements OnInit {
  particle: IParticle;
  particleOp: IParticleOp;

  constructor(protected activatedRoute: ActivatedRoute, protected particleService: ParticleService) {
  }

  ngOnInit() {
    this.activatedRoute.data.subscribe(({particle}) => {
      this.particle = particle;
      this.particleService.view(this.particle.id).subscribe(value => this.particleOp = value.body);
    });
  }

  previousState() {
    window.history.back();
  }

  writeCommand(value: string) {
    this.subscribeToSaveResponse(this.particleService.ctrl(new OpCtrlReq(this.particleOp.id, value)));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
    result.subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.previousState();
  }

  protected onSaveError() {
  }
}
