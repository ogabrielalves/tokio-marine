<template>
  <div class="modal-overlay" v-if="isVisible" @click="closeModal">
    <div class="modal-container" @click.stop>
      <div class="modal-header">
        <h2 class="modal-title">New Transfer</h2>
        <button class="close-btn" @click="closeModal">
          ✕
        </button>
      </div>

      <form @submit.prevent="submitForm" class="modal-body">
        <div v-if="error" class="error-message">
          <div class="error-icon">⚠️</div>
          <span>{{ error }}</span>
        </div>

        <div class="form-group">
          <label class="form-label">
            Conta Origem <span class="required">*</span>
          </label>
          <input type="text" class="form-input" :class="{ error: errors.sourceAccount }" v-model="form.sourceAccount"
            placeholder="Ex: 12345678" maxlength="8" @input="validateSourceAccount" />
          <div v-if="errors.sourceAccount" class="field-error">
            {{ errors.sourceAccount }}
          </div>
          <div class="form-hint">
            Enter 8 digits of the source account
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">
            Conta Destino <span class="required">*</span>
          </label>
          <input type="text" class="form-input" :class="{ error: errors.destinationAccount }"
            v-model="form.destinationAccount" placeholder="Ex: 87654321" maxlength="8"
            @input="validateDestinationAccount" />
          <div v-if="errors.destinationAccount" class="field-error">
            {{ errors.destinationAccount }}
          </div>
          <div class="form-hint">
            Enter 8 digits of the destination account
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">
            Valor <span class="required">*</span>
          </label>
          <div class="input-with-prefix">
            <span class="input-prefix">R$</span>
            <input type="number" class="form-input with-prefix" :class="{ error: errors.amount }"
              v-model.number="form.amount" placeholder="0,00" step="0.01" min="0.01" @input="validateAmount" />
          </div>
          <div v-if="errors.amount" class="field-error">
            {{ errors.amount }}
          </div>
          <div class="form-hint">
            Valor mínimo: R$ 0,01
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">
            Data da Transferência <span class="required">*</span>
          </label>
          <input type="date" class="form-input" :class="{ error: errors.transferDate }" v-model="form.transferDate"
            :min="minDate" @input="validateTransferDate" />
          <div v-if="errors.transferDate" class="field-error">
            {{ errors.transferDate }}
          </div>
          <div class="form-hint">
            The date must be today or a future date
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn-secondary" @click="closeModal" :disabled="loading">
            Cancel
          </button>
          <button type="submit" class="btn-primary" :disabled="loading || !isFormValid">
            <span v-if="loading" class="loading-spinner"></span>
            {{ loading ? 'Scheduling...' : 'Schedule Transfer' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, nextTick } from 'vue';
import type { CreateTransferRequest } from '@/types/transfer';
import { TransferService } from '@/services/transferService';

import { useNotification } from 'naive-ui';

const emit = defineEmits<{
  transferCreated: []
}>();

const isVisible = ref(false);
const loading = ref(false);
const error = ref<string | null>(null);
const notification = useNotification();

const form = ref<CreateTransferRequest>({
  sourceAccount: '',
  destinationAccount: '',
  amount: 0,
  transferDate: ''
});

const errors = ref({
  sourceAccount: '',
  destinationAccount: '',
  amount: '',
  transferDate: ''
});

const minDate = computed(() => {
  return new Date().toISOString().split('T')[0];
});

const isFormValid = computed(() => {
  return (
    form.value.sourceAccount.length === 8 &&
    form.value.destinationAccount.length === 8 &&
    form.value.amount > 0 &&
    form.value.transferDate &&
    !errors.value.sourceAccount &&
    !errors.value.destinationAccount &&
    !errors.value.amount &&
    !errors.value.transferDate
  );
});

const validateSourceAccount = () => {
  const value = form.value.sourceAccount.replace(/\D/g, '');
  form.value.sourceAccount = value;

  if (!value) {
    errors.value.sourceAccount = 'Origin account is mandatory';
  } else if (value.length !== 8) {
    errors.value.sourceAccount = 'Source account must have 8 digits';
  } else if (value === form.value.destinationAccount) {
    errors.value.sourceAccount = 'Source account cannot be the same as the destination account';
  } else {
    errors.value.sourceAccount = '';
  }
};

const validateDestinationAccount = () => {
  const value = form.value.destinationAccount.replace(/\D/g, '');
  form.value.destinationAccount = value;

  if (!value) {
    errors.value.destinationAccount = 'Destination account is mandatory';
  } else if (value.length !== 8) {
    errors.value.destinationAccount = 'Destination account must have 8 digits';
  } else if (value === form.value.sourceAccount) {
    errors.value.destinationAccount = 'Destination account cannot be the same as the source account';
  } else {
    errors.value.destinationAccount = '';
  }
};

const validateAmount = () => {
  if (!form.value.amount || form.value.amount <= 0) {
    errors.value.amount = 'Value must be greater than zero';
  } else {
    errors.value.amount = '';
  }
};

const validateTransferDate = () => {
  if (!form.value.transferDate) {
    errors.value.transferDate = 'Transfer date is mandatory';
  } else {
    const selectedDate = new Date(form.value.transferDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (selectedDate < today) {
      errors.value.transferDate = 'Data deve ser hoje ou uma data futura';
    } else {
      errors.value.transferDate = '';
    }
  }
};

const validateForm = () => {
  validateSourceAccount();
  validateDestinationAccount();
  validateAmount();
  validateTransferDate();
};

const resetForm = () => {
  form.value = {
    sourceAccount: '',
    destinationAccount: '',
    amount: 0,
    transferDate: ''
  };

  errors.value = {
    sourceAccount: '',
    destinationAccount: '',
    amount: '',
    transferDate: ''
  };

  error.value = null;
};

const closeModal = () => {
  isVisible.value = false;
  resetForm();
};

const submitForm = async () => {
  validateForm();

  if (!isFormValid.value) {
    return;
  }

  loading.value = true;
  error.value = null;

  try {
    const response = await TransferService.createTransfer(form.value);

    closeModal();

    notification.success({
      title: 'Scheduled transfer',
      content: response.message || 'Transfer created successfully!',
      duration: 3000
    });

    emit('transferCreated');

  } catch (err: any) {
    const message = err?.message || 'Unknown error';
    notification.error({
      title: 'Error scheduling transfer',
      content: message,
      duration: 4000
    });
    error.value = message;
  } finally {
    loading.value = false;
  }
};

const show = () => {
  resetForm();
  isVisible.value = true;
  nextTick(() => {
    form.value.transferDate = minDate.value;
  });
};

defineExpose({
  show
});
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal-container {
  background-color: white;
  border-radius: 12px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 24px 0 24px;
  border-bottom: 1px solid #f3f4f6;
  margin-bottom: 24px;
  padding-bottom: 20px;
}

.modal-title {
  font-size: 20px;
  font-weight: 600;
  color: #111827;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #6b7280;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: all 0.15s ease;
}

.close-btn:hover {
  background-color: #f3f4f6;
  color: #374151;
}

.modal-body {
  padding: 0 24px 24px 24px;
}

.error-message {
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: #fef2f2;
  border: 1px solid #fecaca;
  color: #dc2626;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 24px;
  font-size: 14px;
}

.error-icon {
  font-size: 16px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #374151;
  margin-bottom: 6px;
}

.required {
  color: #dc2626;
}

.form-input {
  width: 100%;
  padding: 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
  color: #374151;
  transition: all 0.15s ease;
  background-color: white;
}

.form-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.form-input.error {
  border-color: #dc2626;
  box-shadow: 0 0 0 3px rgba(220, 38, 38, 0.1);
}

.input-with-prefix {
  position: relative;
  display: flex;
  align-items: center;
}

.input-prefix {
  position: absolute;
  left: 12px;
  color: #6b7280;
  font-size: 14px;
  font-weight: 500;
  z-index: 1;
}

.form-input.with-prefix {
  padding-left: 40px;
}

.field-error {
  color: #dc2626;
  font-size: 12px;
  margin-top: 4px;
}

.form-hint {
  color: #6b7280;
  font-size: 12px;
  margin-top: 4px;
}

.modal-footer {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 32px;
  padding-top: 20px;
  border-top: 1px solid #f3f4f6;
}

.btn-secondary {
  background-color: white;
  border: 1px solid #d1d5db;
  color: #374151;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.15s ease;
}

.btn-secondary:hover:not(:disabled) {
  background-color: #f9fafb;
  border-color: #9ca3af;
}

.btn-primary {
  background-color: #3b82f6;
  border: 1px solid #3b82f6;
  color: white;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.15s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-primary:hover:not(:disabled) {
  background-color: #2563eb;
  border-color: #2563eb;
}

.btn-primary:disabled,
.btn-secondary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

@media (max-width: 480px) {
  .modal-overlay {
    padding: 10px;
  }

  .modal-header,
  .modal-body {
    padding-left: 16px;
    padding-right: 16px;
  }

  .modal-footer {
    flex-direction: column;
  }

  .btn-secondary,
  .btn-primary {
    width: 100%;
    justify-content: center;
  }
}
</style>
