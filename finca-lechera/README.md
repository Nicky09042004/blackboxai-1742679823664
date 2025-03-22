# Sistema de Gestión - Finca Lechera

Sistema de gestión para fincas lecheras que permite el registro y seguimiento de vacas y su producción de leche.

## Características

- Gestión de vacas (CRUD)
  - Registro de información básica
  - Control de estado
  - Historial de producción
- Registro de producción de leche
  - Producción diaria, semanal y mensual
  - Estadísticas por vaca
  - Reportes de producción
- Exportación de datos a Excel
- Interfaz gráfica intuitiva

## Requisitos del Sistema

- Java 11 o superior
- Maven 3.6 o superior
- Puerto 8080 disponible para el backend
- Conexión a internet (para las dependencias Maven)

## Estructura del Proyecto

```
finca-lechera/
├── backend/               # Servidor Spring Boot
│   ├── src/
│   └── pom.xml
├── frontend/             # Cliente JavaFX
│   ├── src/
│   └── pom.xml
└── pom.xml              # POM padre
```

## Configuración del Proyecto

1. Clonar el repositorio:
```bash
git clone <url-repositorio>
cd finca-lechera
```

2. Compilar el proyecto:
```bash
mvn clean install
```

## Ejecución

1. Iniciar el backend (desde la raíz del proyecto):
```bash
cd backend
mvn spring-boot:run
```

2. Iniciar el frontend (desde otra terminal, en la raíz del proyecto):
```bash
cd frontend
mvn javafx:run
```

## Uso del Sistema

### Gestión de Vacas

1. Abrir la pestaña "Gestión de Vacas"
2. Usar los botones para:
   - "Nueva Vaca": Registrar una nueva vaca
   - "Editar": Modificar datos de una vaca existente
   - "Eliminar": Dar de baja una vaca

### Registro de Producción

1. Abrir la pestaña "Registro de Producción"
2. Usar los botones para:
   - "Nuevo Registro": Añadir nueva producción
   - "Editar": Modificar un registro existente
   - "Eliminar": Eliminar un registro

### Exportación de Datos

1. Hacer clic en "Archivo" > "Exportar Excel"
2. Seleccionar la ubicación para guardar el archivo
3. El archivo Excel contendrá dos hojas:
   - Vacas: Listado de todas las vacas
   - Producción: Registro de producción de leche

## Desarrollo

### Backend (Spring Boot)

- API REST para gestión de datos
- Almacenamiento en Excel usando Apache POI
- Manejo de excepciones centralizado
- Validación de datos

### Frontend (JavaFX)

- Interfaz gráfica moderna con JavaFX
- Patrón MVC
- Comunicación con el backend mediante HTTP
- Validación de formularios

## Solución de Problemas

### Problemas Comunes

1. Error de conexión al backend:
   - Verificar que el puerto 8080 esté disponible
   - Confirmar que el backend esté en ejecución

2. Error al guardar datos:
   - Verificar permisos de escritura en el directorio de la aplicación
   - Comprobar que el archivo Excel no esté abierto en otra aplicación

### Logs

Los logs se encuentran en:
- Backend: `backend/logs/`
- Frontend: `frontend/logs/`

## Contribución

1. Fork del repositorio
2. Crear una rama para la nueva funcionalidad
3. Commit de los cambios
4. Push a la rama
5. Crear un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## Contacto

Para soporte o consultas:
- Email: [correo@ejemplo.com]
- Issues: Crear un issue en el repositorio

## Agradecimientos

- Equipo de desarrollo
- Contribuidores
- Usuarios que proporcionaron feedback