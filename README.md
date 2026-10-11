![Duoc UC](https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png)
# 🧠 Evaluación Final Transversal – Desarrollo Orientado a Objetos II

## 💻 Proyecto: Biblioteca Escolar
## 👤 Autor del proyecto
- **Nombre completo:** Javier Rojas
- **Sección:** PRY2203-001A
- **Carrera:** Analista Programador Computacional
- **Sede:** Online

---

## 📘 Descripción general del sistema
Este proyecto da respuesta a la Evaluación Final Transversal de la asignatura
*Desarrollo Orientado a Objetos II*

Se desarrolló un programa que permite la gestión persistente (CRUD) de Libros, Estudiantes, 
Prestamos mediante interfaz visual además de proveer Reportes, y login de acceso.
La solución desarrollada incorpora uso de Patron Singleton para establecer una única conexión a bbdd.
Se utiliza sincronización de hilos para limitar el registro de préstamos y evitar que la ventana de
prestamos se bloquee.

---

## 🧱 Estructura general del proyecto

```plaintext

database
├── PRY2203_EFT_S9_Script_crea_tablas_biblioteca.sql
├── PRY2203_EFT_S9_Script_poblado_tablas_biblioteca.sql
target
├── reports
│   ├── apidocs
│   │   ├── index.html
src
├── main
│   ├── java
│   │   ├── Main.java
│   │   ├── controlador
│   │   │   ├── ControladorCategorias.java
│   │   │   ├── ControladorEstudiantes.java
│   │   │   ├── ControladorLibros.java
│   │   │   ├── ControladorPrestamos.java
│   │   │   ├── ControladorReportes.java
│   │   │   └── ControladorUsuarios.java
│   │   ├── dao
│   │   │   ├── CategoriaDAO.java
│   │   │   ├── EstudianteDAO.java
│   │   │   ├── LibroDAO.java
│   │   │   ├── PrestamoDAO.java
│   │   │   ├── ReporteDAO.java
│   │   │   ├── UsuarioDAO.java
│   │   │   └── impl
│   │   │       ├── CategoriaDAOImpl.java
│   │   │       ├── EstudianteDAOImpl.java
│   │   │       ├── LibroDAOImpl.java
│   │   │       ├── PrestamoDAOImpl.java
│   │   │       ├── ReporteDAOImpl.java
│   │   │       └── UsuarioDAOImpl.java
│   │   ├── modelo
│   │   │   ├── Categoria.java
│   │   │   ├── Estudiante.java
│   │   │   ├── Libro.java
│   │   │   ├── Prestamo.java
│   │   │   ├── Rol.java
│   │   │   ├── Usuario.java
│   │   │   └── reportes
│   │   │       └── ReporteLibrosMasPrestados.java
│   │   ├── util
│   │   │   └── DatabaseConnection.java
│   │   └── vista
│   │       ├── VentanaGestionEstudiantes.form
│   │       ├── VentanaGestionEstudiantes.java
│   │       ├── VentanaGestionLibros.form
│   │       ├── VentanaGestionLibros.java
│   │       ├── VentanaGestionPrestamos.form
│   │       ├── VentanaGestionPrestamos.java
│   │       ├── VentanaLogin.form
│   │       ├── VentanaLogin.java
│   │       ├── VentanaPrincipalBibliotecario.java
│   │       ├── VentanaPrincipalEstudiante.java
│   │       ├── VentanaReportes.form
│   │       └── VentanaReportes.java
│   └── resources
└── test
    └── java
````

---


## ⚙️ Instrucciones para clonar y ejecutar el proyecto

1. Clone el repositorio desde GitHub:

```bash
git clone https://github.com/jweb93/DuocUC-POO2-EFT.git
```
2. Ejecute los script PRY2203_EFT_S9_Script_crea_tablas_biblioteca.sql y
   PRY2203_EFT_S9_Script_poblado_tablas_biblioteca.sql secuencialmente en una bbdd mysql.

3. Abra el proyecto en IntelliJ IDEA.

4. Actualice la conexión a bbdd editando la clase ConexionBD del paquete util

5. Ejecute el archivo `Main.java` desde la ruta src/main/java.

6.  Puede revisar la documentación del código accediendo al
    archivo `target/reports/apidocs/index.html`

---

**Repositorio GitHub:** https://github.com/jweb93/DuocUC-POO2-EFT
**Fecha de entrega:** \[11/10/2026]

---

© Duoc UC | Escuela de Informática y Telecomunicaciones 




