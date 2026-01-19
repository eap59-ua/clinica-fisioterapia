import api from './api';

export default {
    // Obtener la configuración semanal actual
    obtenerHorarioSemanal() {
        return api.get('/horarios/configuracion');
    },

    // Actualizar un día específico (Lunes, Martes...)
    actualizarDia(dia) {
        return api.put('/horarios/actualizar', dia);
    },

    // Crear un bloqueo (Festivo o Vacaciones)
    crearBloqueo(bloqueo) {
        return api.post('/horarios/bloqueos', bloqueo);
    },

    // Obtener calendario de festivos (para pintarlo luego en la agenda)
    obtenerCalendarioGlobal() {
        return api.get('/horarios/calendario-global');
    }
};