-- Tabla de Usuarios
CREATE TABLE Usuarios (
    id_usuario INTEGER PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    hash_contrasenia VARCHAR(255) NOT NULL,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso TIMESTAMP,
    CONSTRAINT uk_usuario_nombre UNIQUE (nombre_usuario),
    CONSTRAINT uk_usuario_correo UNIQUE (correo)
);

-- Tabla de Perfiles
CREATE TABLE Perfiles (
    id_perfil INTEGER PRIMARY KEY,
    id_usuario INTEGER,
    nombre_completo VARCHAR(100),
    fecha_nacimiento DATE,
    es_cuidador BOOLEAN DEFAULT FALSE,
    preferencias_notificacion JSON,
    idioma VARCHAR(10) DEFAULT 'es',
    preferencia_tema VARCHAR(20) DEFAULT 'light',
    CONSTRAINT fk_perfil_usuario FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- Tabla de Relaciones Cuidador-Paciente
CREATE TABLE CuidadorPaciente (
    id_cuidador INTEGER,
    id_paciente INTEGER,
    tipo_relacion VARCHAR(50),
    fecha_inicio DATE,
    PRIMARY KEY (id_cuidador, id_paciente),
    CONSTRAINT fk_cuidador_perfil FOREIGN KEY (id_cuidador) REFERENCES Perfiles(id_perfil)
);

-- Tabla de Medicamentos
CREATE TABLE Medicamentos (
    id_medicamento INTEGER PRIMARY KEY,
    id_perfil INTEGER,
    nombre VARCHAR(100) NOT NULL,
    dosis VARCHAR(50),
    frecuencia_horas INTEGER,
    fecha_inicio DATE,
    fecha_fin DATE,
    notas TEXT,
    activo BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_medicamento_perfil FOREIGN KEY (id_perfil) REFERENCES Perfiles(id_perfil)
);

-- Tabla de Recordatorios
CREATE TABLE Recordatorios (
    id_recordatorio INTEGER PRIMARY KEY,
    id_medicamento INTEGER,
    hora_programada TIMESTAMP NOT NULL,
    hora_tomado TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'pending',
    hora_pospuesta TIMESTAMP,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recordatorio_medicamento FOREIGN KEY (id_medicamento) REFERENCES Medicamentos(id_medicamento)
);

-- Tabla de Dispositivos
CREATE TABLE Dispositivos (
    id_dispositivo INTEGER PRIMARY KEY,
    id_perfil INTEGER,
    tipo_dispositivo VARCHAR(50),
    token_dispositivo VARCHAR(255),
    esta_activo BOOLEAN DEFAULT TRUE,
    ultima_sincronizacion TIMESTAMP,
    CONSTRAINT fk_dispositivo_perfil FOREIGN KEY (id_perfil) REFERENCES Perfiles(id_perfil)
);