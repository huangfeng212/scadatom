import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmsCharger } from 'app/shared/model/sms-charger.model';

type EntityResponseType = HttpResponse<ISmsCharger>;
type EntityArrayResponseType = HttpResponse<ISmsCharger[]>;

@Injectable({ providedIn: 'root' })
export class SmsChargerService {
    public resourceUrl = SERVER_API_URL + 'api/sms-chargers';

    constructor(protected http: HttpClient) {}

    create(smsCharger: ISmsCharger): Observable<EntityResponseType> {
        return this.http.post<ISmsCharger>(this.resourceUrl, smsCharger, { observe: 'response' });
    }

    update(smsCharger: ISmsCharger): Observable<EntityResponseType> {
        return this.http.put<ISmsCharger>(this.resourceUrl, smsCharger, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISmsCharger>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISmsCharger[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
