import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Subscription} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {IParticle} from 'app/shared/model/particle.model';
import {AccountService} from 'app/core';
import {ParticleService} from './particle.service';

@Component({
  selector: 'jhi-particle',
  templateUrl: './particle.component.html'
})
export class ParticleComponent implements OnInit, OnDestroy {
  particles: IParticle[];
  particleOps = {};
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
      protected particleService: ParticleService,
      protected jhiAlertService: JhiAlertService,
      protected eventManager: JhiEventManager,
      protected accountService: AccountService
  ) {
  }

  loadAll() {
    this.particleService
    .query()
    .pipe(
        filter((res: HttpResponse<IParticle[]>) => res.ok),
        map((res: HttpResponse<IParticle[]>) => res.body)
    )
    .subscribe(
        (res: IParticle[]) => {
          this.particles = res;
          this.particles.forEach(value => {
            this.particleService.view(value.id).subscribe(value1 => {
              this.particleOps[value1.body.id] = value1.body;
              console.log(this.particleOps)
            }, error1 => {
              console.log(error1.error.detail)
            })
          })
        },
        (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInParticles();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IParticle) {
    return item.id;
  }

  registerChangeInParticles() {
    this.eventSubscriber = this.eventManager.subscribe('particleListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
