/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { InjectionToken } from "@angular/core";
import { SharedInfo } from "../models/shared-info";

export const Config = new InjectionToken<SharedInfo>('ProfileType');