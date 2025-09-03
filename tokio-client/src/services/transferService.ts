import axios from 'axios';
import type { Transfer, CreateTransferRequest, TransferResponse, CreateTransferResponse } from '@/types/transfer';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export class TransferService {
  static async getTransfers(page: number = 0, size: number = 10): Promise<TransferResponse> {
    try {
      const response = await api.get<TransferResponse>('/transfers', {
        params: {
          page,
          size,
        },
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching transfers:', error);
      throw new Error('Error fetching transfers');
    }
  }


  static async createTransfer(transfer: CreateTransferRequest): Promise<CreateTransferResponse> {
    try {
      const { data } = await api.post<CreateTransferResponse>('/transfers', transfer);
      return data;
    } catch (error: any) {
      console.error('Failed to create transfer:', error);
      throw new Error(
        error?.response?.data?.message || 'Failed to create transfer'
      );
    }
  }

}

export default TransferService;

