/*************************************************
Copyright Regione Piemonte - 2024
SPDX-License-Identifier: EUPL-1.2-or-later
***************************************************/
export const DOWNLOAD_FILE_NAME = {
    RICERCA_VE: "ricerca-versamento-esoneri",
    DICHIARAZIONE_VERSAMENTO: "dichiarazione-di-versamento",
    TEMPLATE_PREVISIONI: "template-previsioni",
    AVVISO_PAGAMENTO: "avviso-pagamento",
}
export const MONTH = {
    GENNAIO: 0,
    FEBBRAIO: 1,
    MARZO: 2,
    APRILE: 3,
    MAGGIO: 4,
    GIUGNO: 5,
    LUGLIO: 6,
    AGOSTO: 7,
    SETTEMBRE: 8,
    OTTOBRE: 9,
    NOVEMBRE: 10,
    DICEMBRE: 11
}

export const TYPE_ALERT = {
    SUCCESS: 'S',
    ERROR: 'E',
    WARNING: 'W',
    INFO: 'I',
    VALIDATION: 'V'
};

export const ROUTE = {
    MODULE: {

        APP:{
            ROOT: '',
            ACCESSO_RUOLO: 'accesso-ruolo'
        },
        COLMIR: {
            ROOT: 'colmir',
            HOME_PAGE: 'home-page'
        }
    }
}

export const STORAGE_KEY = {
    LOCAL: {

    },
    SESSION: {
        UTENTE: 'utente',
        RUOLO: 'ruolo',
        MESSAGGI: 'messaggi',
        WIZARD_MODE: 'mode',
        CURRENT_DATE: 'data',
        VERSAMENTO: 'versamento'
    }
}

export const HTTP_RESPONSE = {
    SATUS: {
        UNKNOW_ERROR: 0,
        NOT_FOUND: 404,
        INTERNAL_SERVER_ERROR: 500
    }
}

export const FUNZIONI = {
    DICHIARAZIONE_VERSAMENTO_ESONERI: {
        ID: 1
    },
    AUTORIZZA_DICHIARAZIONI: {
        ID: 7
    },
    PREVISIONE_DICHIARAZIONI: {
        ID: 8
    },
    GENERA_AVVISI_PAGAMENTO: {
        ID: 9
    }
}

export const RUOLO = {
    AMMINISTRATORE: {
        ID: 1
    },
    REGIONE: {
        ID: 2
    },
    DELEGATO: {
        ID: 4
    },
    CONSULENTE: {
        ID: 3
    },
    PERSONA_AUTORIZZATA: {
        ID: 5
    },
    //ruolo legale rappresentante, persona con carica aziendale,
    LEGALE_RAPPRESENTANTE: {
        ID: 6
    }
}

export const CONTROL_STATE = {
    DISABLE: 'disable',
    ENABLE: 'enable'
  };

export const ICONA_BASE: string = "fas fa-screwdriver";

export const HEADER_KEYS = {
    ID_RUOLO_KEY: "x-ruolo",
    COD_FISC_SOGG_ABILITATO: "x-cf-abilitato"
}

export const WIZARD_MODE = {
    INS: "ins",
    EDIT: "edit",
    VIEW: "view",
    DUPLICATE: "duplicate",
}

export const STATO = {
    BOZZA: {
        ID: 1,
        DS: "Bozza"
    },
    ACCETTATA: {
        ID: 2,
        DS: "Accettata"
    },
    MODIFICATA: {
        ID: 3,
        DS: "Modificata"
    },
    AUTORIZZATA: {
        ID: 4,
        DS: "Autorizzata"
    },
    NON_AUTORIZZATA: {
        ID: 5,
        DS: "Non autorizzata"
    },
    ANNULLATA: {
        ID: 6,
        DS: "Annullata"
    },
    ELIMINATA: {
        ID: 7,
        DS: "Eliminata"
    },
    AVVISO_INVIATO: {
        ID: 8,
        DS: 'Avviso inviato'
    },
    ACCONTO: {
        ID: 9,
        DS: "Acconto",
    },
    SALDO: {
        ID: 10,
        DS: "Saldo",
    }
}

export const COMPENSAZIONE_DISABILI = {
    RIDUZIONE: 'R',
    ECCESSO: 'E',
    COMPENSAZIONE: 'C'
}

export const TIPO_PERIODO = {
    AUTOMATICO: 'A',
    CANCELLATO: 'C',
    OPERATORE: 'O'
}

export const CATEGORIA_AZIENDA = {
    A:{
        COD: "A",
        ID: 1
    },
    B: {
        COD: "B",
        ID: 2
    },
    C: {
        COD: "C",
        ID: 3
    }
}

export const AMBIENTE = {
    DEV: 'local',
    TEST: 'test-rp-01',
    PROD: 'prod-rp-01'
}

export const APP_PARAMAS = {
    PAGINAZIONE: {
        LIMIT: 5
    }
}
