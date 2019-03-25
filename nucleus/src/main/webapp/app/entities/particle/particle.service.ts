import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IParticle } from 'app/shared/model/particle.model';

type EntityResponseType = HttpResponse<IParticle>;
type EntityArrayResponseType = HttpResponse<IParticle[]>;

@Injectable({ providedIn: 'root' })
export class ParticleService {
    public resourceUrl = SERVER_API_URL + 'api/particles';

    constructor(protected http: HttpClient) {}

    create(particle: IParticle): Observable<EntityResponseType> {
        return this.http.post<IParticle>(this.resourceUrl, particle, { observe: 'response' });
    }

    update(particle: IParticle): Observable<EntityResponseType> {
        return this.http.put<IParticle>(this.resourceUrl, particle, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IParticle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IParticle[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
