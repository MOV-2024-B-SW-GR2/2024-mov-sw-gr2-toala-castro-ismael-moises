# Historias de Usuario - Medication_reminder_App

## **HU-01: Registro de Medicamento**

**Como** paciente  
**Quiero** registrar un nuevo medicamento en el sistema  
**Para que** pueda mantener un control detallado de mis medicamentos y horarios de toma.  

### **Criterios de Aceptación**  

- [ ] Debe permitir ingresar el nombre del medicamento de forma clara y precisa.  
- [ ] Debe permitir especificar la dosis, incluyendo unidades como "mg", "ml", etc.  
- [ ] Debe permitir establecer la frecuencia (por ejemplo, cada 8 horas o dos veces al día).  
- [ ] Debe permitir definir una fecha de inicio y una fecha de fin.  
- [ ] Debe mostrar un mensaje emergente con el texto: "Medicamento registrado exitosamente."  
- [ ] El medicamento registrado debe aparecer en la lista de medicamentos activos inmediatamente después del registro.  


### **Escenario de Prueba**  

1. El usuario accede a la opción "Agregar Medicamento" en la aplicación.  
2. Rellena todos los campos del formulario con datos válidos (nombre, dosis, frecuencia, fechas).  
3. Presiona el botón "Guardar".  
4. El sistema confirma el registro exitoso con un mensaje en pantalla.  
5. El medicamento aparece en la lista de medicamentos activos.  

---

## **HU-02: Gestión de Recordatorios**

**Como** paciente  
**Quiero** recibir notificaciones cuando sea momento de tomar un medicamento  
**Para que** no olvide cumplir con mi tratamiento.  

### **Criterios de Aceptación**  

- [ ] Debe notificar al usuario en los horarios configurados previamente.  
- [ ] Debe permitir al usuario marcar el medicamento como "tomado".  
- [ ] La notificación debe incluir:
  - Nombre del medicamento.
  - Dosis específica (por ejemplo, "2 pastillas de 500 mg").
  - Tiempo exacto programado.  
- [ ] Debe permitir posponer la notificación en intervalos de 5, 10 y 15 minutos con un botón interactivo.  
- [ ] Las notificaciones deben funcionar incluso si la aplicación está cerrada o en segundo plano.  

### **Escenario de Prueba**  

1. Llega la hora programada para tomar un medicamento.  
2. El sistema envía una notificación con el nombre del medicamento y su dosis.  
3. El usuario interactúa con la notificación (marca como "tomado" o pospone).  
4. El sistema registra la acción realizada por el usuario y actualiza el estado del medicamento.  

---

## **HU-03: Vista de Cuidador**

**Como** cuidador  
**Quiero** gestionar los medicamentos de los pacientes bajo mi cuidado  
**Para que** pueda asegurarme de que están siguiendo correctamente su tratamiento.  

### **Criterios de Aceptación**  

- [ ] Debe mostrar una lista de pacientes asignados al cuidador.  
- [ ] Debe permitir visualizar los medicamentos registrados para cada paciente.  
- [ ] Debe ofrecer opciones para editar la información de los medicamentos (dosis, frecuencia, fechas).  
- [ ] Las notificaciones deben enviarse una vez transcurridos 15 minutos del horario programado, incluyendo:
  - Nombre del paciente.
  - Nombre y dosis del medicamento omitido.
  - Hora exacta en que debía ser tomado.  
- [ ] Debe incluir acceso al historial de cumplimiento de medicamentos por paciente.  

### **Escenario de Prueba**  

1. El cuidador accede a la sección de "Pacientes" desde la aplicación.  
2. Selecciona un paciente de la lista de asignados.  
3. Visualiza los medicamentos registrados para ese paciente y realiza una actualización en uno de ellos.  
4. El sistema guarda los cambios y los notifica al paciente.  
5. El cuidador recibe una notificación si el paciente no registra una toma en el horario indicado.  

---

## **HU-04: Historial de Medicamentos**

**Como** paciente/cuidador  
**Quiero** acceder al historial de medicamentos tomados  
**Para que** pueda llevar un control claro del cumplimiento del tratamiento.  

### **Criterios de Aceptación**  

- [ ] Debe mostrar las fechas y horas exactas en que se tomaron los medicamentos.  
- [ ] Debe indicar claramente qué medicamentos se tomaron y cuáles fueron omitidos.  
- [ ] Debe permitir filtrar el historial por rango de fechas.  
- [ ] Debe permitir filtrar el historial por nombre del medicamento.  
- [ ] Debe mostrar estadísticas de cumplimiento (porcentaje de dosis tomadas vs. omitidas).  
- [ ] Debe ofrecer la opción de exportar el historial en formatos PDF y Excel.  
- [ ] El archivo exportado debe incluir:
  - Nombre completo del paciente.
  - Fechas, horas y estado (tomado/omitido) de cada medicamento.
  - Estadísticas de cumplimiento al final del documento.  


### **Escenario de Prueba**  

1. El usuario accede a la sección de "Historial de Medicamentos".  
2. Aplica un filtro por fecha y medicamento específico.  
3. Visualiza un listado detallado de las tomas realizadas, con estadísticas de cumplimiento.  
4. Selecciona la opción "Exportar" y el sistema genera un archivo en el formato deseado.  

---

## **Notas de Priorización SCRUM**  

### **Sprint 1**  

- HU-01: Registro de Medicamento.  
- HU-02: Gestión de Recordatorios (funcionalidad básica de notificaciones).  

### **Sprint 2**  

- HU-03: Vista de Cuidador.  
- Mejoras en HU-02 (opciones avanzadas de notificaciones).  

### **Sprint 3**  

- HU-04: Historial de Medicamentos.  
- Refinamientos y optimización de funcionalidades previas.  
