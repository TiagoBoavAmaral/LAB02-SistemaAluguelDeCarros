// Custom JavaScript for Car Rental System

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
        alerts.forEach(function(alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);

    // Form validation
    var forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // CPF and CNPJ formatting
    formatCpfCnpj();
    
    // Phone formatting
    formatPhone();
    
    // Date validation
    validateDates();
    
    // Confirmation dialogs
    setupConfirmationDialogs();
    
    // Search functionality
    setupSearch();
    
    // Dynamic form updates
    setupDynamicForms();
});

// CPF and CNPJ formatting
function formatCpfCnpj() {
    // CPF formatting
    var cpfInputs = document.querySelectorAll('input[name*="cpf"], input[id*="cpf"]');
    cpfInputs.forEach(function(input) {
        input.addEventListener('input', function(e) {
            var value = e.target.value.replace(/\D/g, '');
            if (value.length <= 11) {
                value = value.replace(/(\d{3})(\d)/, '$1.$2');
                value = value.replace(/(\d{3})(\d)/, '$1.$2');
                value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
            }
            e.target.value = value;
        });
    });

    // CNPJ formatting
    var cnpjInputs = document.querySelectorAll('input[name*="cnpj"], input[id*="cnpj"]');
    cnpjInputs.forEach(function(input) {
        input.addEventListener('input', function(e) {
            var value = e.target.value.replace(/\D/g, '');
            if (value.length <= 14) {
                value = value.replace(/^(\d{2})(\d)/, '$1.$2');
                value = value.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
                value = value.replace(/\.(\d{3})(\d)/, '.$1/$2');
                value = value.replace(/(\d{4})(\d)/, '$1-$2');
            }
            e.target.value = value;
        });
    });
}

// Phone formatting
function formatPhone() {
    var phoneInputs = document.querySelectorAll('input[type="tel"], input[name*="phone"], input[id*="phone"]');
    phoneInputs.forEach(function(input) {
        input.addEventListener('input', function(e) {
            var value = e.target.value.replace(/\D/g, '');
            if (value.length <= 11) {
                if (value.length <= 10) {
                    value = value.replace(/(\d{2})(\d)/, '($1) $2');
                    value = value.replace(/(\d{4})(\d)/, '$1-$2');
                } else {
                    value = value.replace(/(\d{2})(\d)/, '($1) $2');
                    value = value.replace(/(\d{5})(\d)/, '$1-$2');
                }
            }
            e.target.value = value;
        });
    });
}

// Date validation
function validateDates() {
    var startDateInputs = document.querySelectorAll('input[name*="startDate"], input[id*="startDate"]');
    var endDateInputs = document.querySelectorAll('input[name*="endDate"], input[id*="endDate"]');
    
    function validateDateRange() {
        startDateInputs.forEach(function(startInput, index) {
            var endInput = endDateInputs[index];
            if (startInput && endInput) {
                var startDate = new Date(startInput.value);
                var endDate = new Date(endInput.value);
                var today = new Date();
                today.setHours(0, 0, 0, 0);
                
                // Start date cannot be in the past
                if (startDate < today) {
                    startInput.setCustomValidity('Data de início não pode ser no passado');
                } else {
                    startInput.setCustomValidity('');
                }
                
                // End date must be after start date
                if (endDate <= startDate) {
                    endInput.setCustomValidity('Data de fim deve ser posterior à data de início');
                } else {
                    endInput.setCustomValidity('');
                }
            }
        });
    }
    
    startDateInputs.forEach(function(input) {
        input.addEventListener('change', validateDateRange);
    });
    
    endDateInputs.forEach(function(input) {
        input.addEventListener('change', validateDateRange);
    });
}

// Confirmation dialogs
function setupConfirmationDialogs() {
    var deleteButtons = document.querySelectorAll('[data-confirm]');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            var message = button.getAttribute('data-confirm') || 'Tem certeza que deseja realizar esta ação?';
            if (!confirm(message)) {
                e.preventDefault();
                return false;
            }
        });
    });
}

// Search functionality
function setupSearch() {
    var searchInputs = document.querySelectorAll('.search-input');
    searchInputs.forEach(function(input) {
        var searchForm = input.closest('form');
        var timeout;
        
        input.addEventListener('input', function() {
            clearTimeout(timeout);
            timeout = setTimeout(function() {
                if (searchForm) {
                    searchForm.submit();
                }
            }, 500);
        });
    });
}

// Dynamic form updates
function setupDynamicForms() {
    // Car selection updates total amount
    var carSelects = document.querySelectorAll('select[name="car"], select[id*="car"]');
    var startDateInputs = document.querySelectorAll('input[name="startDate"]');
    var endDateInputs = document.querySelectorAll('input[name="endDate"]');
    
    function updateTotalAmount() {
        var carSelect = document.querySelector('select[name="car"]');
        var startDateInput = document.querySelector('input[name="startDate"]');
        var endDateInput = document.querySelector('input[name="endDate"]');
        var totalAmountDisplay = document.querySelector('#totalAmount');
        
        if (carSelect && startDateInput && endDateInput && totalAmountDisplay) {
            var selectedOption = carSelect.options[carSelect.selectedIndex];
            var dailyRate = selectedOption.getAttribute('data-daily-rate');
            
            if (dailyRate && startDateInput.value && endDateInput.value) {
                var startDate = new Date(startDateInput.value);
                var endDate = new Date(endDateInput.value);
                var days = Math.ceil((endDate - startDate) / (1000 * 60 * 60 * 24)) + 1;
                
                if (days > 0) {
                    var total = parseFloat(dailyRate) * days;
                    totalAmountDisplay.textContent = 'R$ ' + total.toFixed(2).replace('.', ',');
                }
            }
        }
    }
    
    carSelects.forEach(function(select) {
        select.addEventListener('change', updateTotalAmount);
    });
    
    startDateInputs.forEach(function(input) {
        input.addEventListener('change', updateTotalAmount);
    });
    
    endDateInputs.forEach(function(input) {
        input.addEventListener('change', updateTotalAmount);
    });
}

// Utility functions
function showLoading(button) {
    var originalText = button.innerHTML;
    button.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status"></span>Carregando...';
    button.disabled = true;
    
    return function() {
        button.innerHTML = originalText;
        button.disabled = false;
    };
}

function formatCurrency(value) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(value);
}

function formatDate(dateString) {
    var date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
}

// Status badge helper
function getStatusBadgeClass(status) {
    var statusClasses = {
        'PENDING': 'bg-warning text-dark',
        'UNDER_EVALUATION': 'bg-info text-dark',
        'APPROVED': 'bg-success',
        'REJECTED': 'bg-danger',
        'CANCELLED': 'bg-secondary',
        'ACTIVE': 'bg-primary',
        'COMPLETED': 'bg-dark',
        'AVAILABLE': 'bg-success',
        'RENTED': 'bg-danger',
        'MAINTENANCE': 'bg-warning text-dark',
        'UNAVAILABLE': 'bg-secondary'
    };
    
    return statusClasses[status] || 'bg-secondary';
}

// Export functions for global use
window.CarRentalApp = {
    showLoading: showLoading,
    formatCurrency: formatCurrency,
    formatDate: formatDate,
    getStatusBadgeClass: getStatusBadgeClass
};

