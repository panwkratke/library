import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Dictionary } from '../model/dictionary';
import { Observable, BehaviorSubject } from 'rxjs';
import { ApiResponse } from '../model/apiResponse';
import { RestApiUrlConfig } from '../config/config';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DictionaryService {

  private currentDictionarySubject: BehaviorSubject<Dictionary>;
  public currentDictionary: Observable<Dictionary>;

  constructor(private http: HttpClient) {
    this.currentDictionarySubject = new BehaviorSubject<Dictionary>(JSON.parse(localStorage.getItem('dictionary')));
    this.currentDictionary = this.currentDictionarySubject.asObservable();
  }

  public initDictionaryByDictioanryName(dictionaryName: string): Observable<any> {
    return this.http.get<any>(RestApiUrlConfig.REST_API_DICTIONARY_GET_BY_DICTIONARY_NAME + dictionaryName)
    .pipe(map((apiResp: ApiResponse<Dictionary>) => {
      localStorage.setItem('dictionary', JSON.stringify(apiResp.content));
      this.currentDictionarySubject.next(apiResp.content);
      return apiResp.content;
    }));
  }

  public get currentDictionaryValue(): Dictionary {
    return this.currentDictionarySubject.value;
  }

  public getDictionaryByDictioanryName(dictionaryName: string): Observable<ApiResponse<Dictionary>> {
    return this.http.get<ApiResponse<Dictionary>>(RestApiUrlConfig.REST_API_DICTIONARY_GET_BY_DICTIONARY_NAME + dictionaryName);
  }
}
