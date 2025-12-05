import { HttpInterceptorFn } from '@angular/common/http';



// Le but attacher automatiquement le token à toutes les requêtes
export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req);
};
