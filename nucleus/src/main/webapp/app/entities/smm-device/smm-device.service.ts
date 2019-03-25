import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmmDevice } from 'app/shared/model/smm-device.model';

type EntityResponseType = HttpResponse<ISmmDevice>;
type EntityArrayResponseType = HttpResponse<ISmmDevice[]>;

@Injectable({ providedIn: 'root' })
export class SmmDeviceService {
    public resourceUrl = SERVER_API_URL + 'api/smm-devices';

    constructor(protected http: HttpClient) {}

    create(smmDevice: ISmmDevice): Observable<EntityResponseType> {
        return this.http.post<ISmmDevice>(this.resourceUrl, smmDevice, { observe: 'response' });
    }

    update(smmDevice: ISmmDevice): Observable<EntityResponseType> {
        return this.http.put<ISmmDevice>(this.resourceUrl, smmDevice, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISmmDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISmmDevice[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
