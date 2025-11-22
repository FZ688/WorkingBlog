const fs = require('fs');
const path = require('path');

const root = path.resolve(__dirname, '..');
const targets = [
  path.join(root, 'node_modules'),
  path.join(root, 'node_modules', 'node-sass'),
  path.join(root, 'node_modules', 'node-gyp'),
  path.join(root, 'node_modules', 'node-notifier', 'vendor')
];

function removeTarget(p) {
  try {
    if (!fs.existsSync(p)) return console.log('[clean-residuals] not found:', p);
    // fs.rmSync is available in modern Node; fallback to rmdirSync if needed
    if (fs.rmSync) {
      fs.rmSync(p, { recursive: true, force: true });
    } else {
      fs.rmdirSync(p, { recursive: true });
    }
    console.log('[clean-residuals] removed:', p);
  } catch (err) {
    console.error('[clean-residuals] failed to remove:', p, err && err.message ? err.message : err);
  }
}

console.log('[clean-residuals] starting cleanup...');
for (const t of targets) removeTarget(t);
console.log('[clean-residuals] finished');
