import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmmCharger } from 'app/shared/model/smm-charger.model';

type EntityResponseType = HttpResponse<ISmmCharger>;
type EntityArrayResponseType = HttpResponse<ISmmCharger[]>;

@Injectable({ providedIn: 'root' })
export class SmmChargerService {
    public resourceUrl = SERVER_API_URL + 'api/smm-chargers';

    constructor(protected http: HttpClient) {}

    create(smmCharger: ISmmCharger): Observable<EntityResponseType> {
        return this.http.post<ISmmCharger>(this.resourceUrl, smmCharger, { observe: 'response' });
    }

    update(smmCharger: ISmmCharger): Observable<EntityResponseType> {
        return this.http.put<ISmmCharger>(this.resourceUrl, smmCharger, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISmmCharger>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISmmCharger[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
