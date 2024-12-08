# Diagrama Entidad Relación - Medication Reminder App

```mermaid
erDiagram
    Usuarios ||--o{ Perfiles : tiene
    Perfiles ||--o{ Medicamentos : administra
    Perfiles ||--o{ Dispositivos : utiliza
    Medicamentos ||--o{ Recordatorios : genera
    Perfiles }|--|| CuidadorPaciente : tiene
    

    Usuarios {
        int id_usuario PK
        string nombre_usuario UK
        string correo UK
        string hash_contrasenia
        timestamp creado_en
        timestamp ultimo_acceso
    }

    Perfiles {
        int id_perfil PK
        int id_usuario FK
        string nombre_completo
        date fecha_nacimiento
        boolean es_cuidador
        json preferencias_notificacion
        string idioma
        string preferencia_tema
    }

    CuidadorPaciente {
        int id_cuidador PK,FK
        int id_paciente PK,FK
        string tipo_relacion
        date fecha_inicio
    }

    Medicamentos {
        int id_medicamento PK
        int id_perfil FK
        string nombre
        string dosis
        int frecuencia_horas
        date fecha_inicio
        date fecha_fin
        text notas
        boolean activo
        timestamp creado_en
    }

    Recordatorios {
        int id_recordatorio PK
        int id_medicamento FK
        timestamp hora_programada
        timestamp hora_tomado
        string estado
        timestamp hora_pospuesta
        timestamp creado_en
    }

    Dispositivos {
        int id_dispositivo PK
        int id_perfil FK
        string tipo_dispositivo
        string token_dispositivo
        boolean esta_activo
        timestamp ultima_sincronizacion
    }
