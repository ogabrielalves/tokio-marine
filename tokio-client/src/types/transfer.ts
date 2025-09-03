export interface Transfer {
  id: number;
  sourceAccount: string;
  destinationAccount: string;
  amount: number;
  fee: number;
  transferDate: string;
  schedulingDate: string;
}

export interface CreateTransferRequest {
  sourceAccount: string;
  destinationAccount: string;
  amount: number;
  transferDate: string;
}

export interface CreateTransferResponse {
  message: string;
}

export interface TransferResponse {
  content: Transfer[];
  pageable: {
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    pageSize: number;
    pageNumber: number;
    unpaged: boolean;
    paged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  first: boolean;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  numberOfElements: number;
  empty: boolean;
}

