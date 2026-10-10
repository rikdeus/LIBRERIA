# Guía de Git y GitHub del equipo

## Estrategia de ramas (Git Flow simplificado)

| Rama | Uso |
|------|-----|
| `main` | Versión estable. Solo se actualiza desde `develop` mediante Pull Request. |
| `develop` | Integración del trabajo del equipo. |
| `feature/<tema>` | Una función nueva (ej. `feature/reporte-ventas`). Sale de `develop`. |
| `fix/<tema>` | Corrección de errores. |
| `docs/<tema>` | Cambios solo de documentación. |

## Flujo de trabajo de cada integrante

```bash
git checkout develop && git pull
git checkout -b feature/mi-tema
# ... trabajar ...
git add <archivos>
git commit -m "feat: agrega reporte de ventas por día"
git push -u origin feature/mi-tema
```
Luego abrir un **Pull Request** hacia `develop`, pedir que otro compañero lo revise y fusionar solo con aprobación.

## Convención de mensajes de commit

`tipo: descripción corta en presente`

| Tipo | Cuándo |
|------|--------|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de un error |
| `docs` | Documentación |
| `refactor` | Reorganizar código sin cambiar su comportamiento |
| `test` | Pruebas |
| `chore` | Tareas de mantenimiento (limpieza, configuración) |

## Convención de nombres en el código

Clases en `PascalCase`, métodos y variables en `camelCase`, constantes en `MAYUSCULAS_CON_GUION`, paquetes en minúscula.

## Comandos avanzados (para la evidencia de la semana 3)

```bash
git stash            # guarda cambios sin commitear para cambiar de rama
git stash pop        # los recupera
git revert <hash>    # crea un commit que deshace otro, sin reescribir historia
git cherry-pick <hash>  # copia un commit de otra rama
```

## Resolución de conflictos
1. `git merge develop` desde tu rama. 2. Abrir los archivos marcados con `<<<<<<<`.
3. Dejar la versión correcta y borrar las marcas. 4. `git add` y `git commit`.
