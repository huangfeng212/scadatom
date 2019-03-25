import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmsDevice } from 'app/shared/model/sms-device.model';

type EntityResponseType = HttpResponse<ISmsDevice>;
type EntityArrayResponseType = HttpResponse<ISmsDevice[]>;

@Injectable({ providedIn: 'root' })
export class SmsDeviceService {
    public resourceUrl = SERVER_API_URL + 'api/sms-devices';

    constructor(protected http: HttpClient) {}

    create(smsDevice: ISmsDevice): Observable<EntityResponseType> {
        return this.http.post<ISmsDevice>(this.resourceUrl, smsDevice, { observe: 'response' });
    }

    update(smsDevice: ISmsDevice): Observable<EntityResponseType> {
        return this.http.put<ISmsDevice>(this.resourceUrl, smsDevice, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISmsDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISmsDevice[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
