// Service worker de notificaciones push (Firebase Cloud Messaging).
// El backend manda mensajes "data" con: titulo, mensaje, url, idNotificacion.
self.addEventListener('push', (event) => {
  let payload = {};
  try { payload = event.data ? event.data.json() : {}; } catch (e) { payload = {}; }
  const data = payload.data || {};
  const noti = payload.notification || {};
  const titulo = data.titulo || noti.title || 'Lost & Found IA';
  const cuerpo = data.mensaje || noti.body || 'Tienes una nueva notificación';

  event.waitUntil((async () => {
    await self.registration.showNotification(titulo, {
      body: cuerpo,
      icon: '/favicon.ico',
      tag: 'lf-' + (data.idNotificacion || Date.now()),
      data: { url: data.url || '/notificaciones' },
    });
    // Avisa a las pestañas abiertas para que actualicen la campanita
    const pestañas = await self.clients.matchAll({ type: 'window', includeUncontrolled: true });
    pestañas.forEach((c) => c.postMessage({ tipo: 'lf-push' }));
  })());
});

// Al hacer clic en la notificación se abre (o enfoca) la página de notificaciones
self.addEventListener('notificationclick', (event) => {
  event.notification.close();
  const url = (event.notification.data && event.notification.data.url) || '/notificaciones';
  event.waitUntil((async () => {
    const pestañas = await self.clients.matchAll({ type: 'window', includeUncontrolled: true });
    for (const c of pestañas) {
      if ('focus' in c) { await c.navigate(url).catch(() => {}); return c.focus(); }
    }
    return self.clients.openWindow(url);
  })());
});
