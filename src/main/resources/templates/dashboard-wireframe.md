Title: MedLink Modern Dashboard UI — Wireframe and Annotations

Overview
A refresh of the Client Dashboard that preserves all current workflows (map, doctor search/filter, details navigation) while modernizing visuals, layout, interactivity, data viz and accessibility.

Global Design Tokens
- Color: brand #0d9488 (teal), brand-600 #0f766e, accent #2dd4bf, surface #ffffff, text #0f172a, muted #6b7280, border rgba(15,23,42,0.08).
- Dark mode: complementary darker surfaces, text, borders; respects prefers-color-scheme.
- Typography: Inter (300/400/600/700). Base 16px, fluid type via clamp.
- Spacing: 4/8/12/16/24/32 scale. Radius 10px, shadows soft.

Layout and Structure
- App grid: 260px sidebar, 1fr content. On ≤1024px collapsible sidebar; on ≤768px turn into top nav.
- Content grid: responsive 12-col CSS grid with cards auto-fit (minmax(280px, 1fr)).
- Sticky sidebar for filters; maintains current search actions and statuses.

Navigation
- Sidebar groups: Dashboard (home), My Appointments, Doctors, Messages, Settings.
- Active state via background tint and left accent bar; icons with labels; large touch targets.
- CTA buttons: primary "Search" and secondary "Clear" persist at top of filter section.

Key Screens/Areas
1) Overview Header
- Left: greeting, user name, quick actions (Book Appointment, Find Nearby). Right: theme toggle, profile menu.
- Breadcrumbs contextual for subpages.

2) Doctors + Map
- Two-panel: left list (virtualized if long), right Leaflet map.
- Cards include avatar, name, specialty, rating, distance, address; actions: Locate, Details. Preserves current behavior.
- Hover list item highlights corresponding map marker; marker popup shows same info.

3) Stats Cards (optional, non-blocking)
- Small cards: Upcoming Appointments, New Messages, Favorites, Avg Rating.
- Sparkline area charts; non-interfering and can be hidden if data not available.

4) Messages/Notifications
- Inline bell icon with badge; slide-in panel lists latest notifications; read/unread states.

Interactive Components
- Buttons: primary (brand), secondary (surface), subtle (ghost). 200ms transitions, focus ring 3px teal.
- Inputs: clear label, helper text, validation states; larger hit areas.
- Dropdowns/Sliders: consistent height, keyboard navigable; ARIA roles.
- Hover: elevate cards slightly; highlight list row; map marker pulse.

Data Visualization
- Charts: use Chart.js (bar/line/donut). Colors match tokens; gridlines muted.
- Tooltips: concise labels, values, units; legends clickable to toggle series.
- Empty states: placeholders with guidance.

Accessibility (WCAG 2.1 AA)
- Contrast: primary on white meets AA; dark mode equivalents.
- Keyboard: tab order logical; skip-to-content link; visible focus states.
- ARIA: roles on nav, buttons, list items, map landmark; descriptive labels.
- Preferences: text size control (90–120%), high-contrast toggle; respects OS theme.

Functionality Preservation
- Filters: keep IDs (doc-search, doc-specialty, doc-distance, doc-rating, doc-search-btn, doc-clear-btn, doc-status) to avoid JS changes.
- Map: Leaflet init and marker logic unchanged; list/map interactions enhanced via hover only.
- Details: maintain /doctors/{id} navigation; zero change to endpoints.
- Performance: no blocking reflows; defer charts; lazy-load avatars.

Wireframe (ASCII)

[Top App Bar]
| MedLink | Search (global) | Theme Toggle | Profile |

[Content]
| Sidebar (sticky)                 | Main Content                            |
| - Brand + Nav                    | - Header: greeting + quick actions      |
| - Filters                        | - Stats cards (optional)                |
|   - Query                        | - Doctors + Map split                   |
|   - Specialty, Distance, Rating  |   | List (cards) | Map (Leaflet) |      |
|   - CTA: Search / Clear          |                                          |

Doctor Card
-----------------------------------------------
| Avatar | Name • Specialty          ★4.7  3km |
| Address                               [Locate] [Details] |
-----------------------------------------------

Key Annotations
- Keep existing element IDs and data attributes so JS continues to work.
- Add data-hover hooks for list-map highlighting without breaking clicks.
- Use CSS prefers-reduced-motion to respect motion-sensitive users.
- Ensure forms have associated <label> with for/id.

Implementation Notes (incremental)
- CSS: augment existing <style> with prefers-color-scheme, fluid type, focus rings.
- HTML: wrap filters in <form aria-label="Doctor filters">; add role="navigation" to sidebar.
- JS: optional: add mouseenter/mouseleave to list items to open marker popup; no API changes.

Assets
- Icons: Bootstrap Icons (already included).
- Charts: Chart.js via CDN, loaded only on dashboard route; non-blocking.

Testing Checklist
- Keyboard-only navigation covers all controls.
- Screen reader announces nav, list items, buttons, map region.
- Light/dark/high-contrast/test-resize behaviors verified.
- Existing search, locate, details workflows validated.
