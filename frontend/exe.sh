#!/bin/bash

echo "Creating Sass architecture..."

# Create folders
mkdir -p src/styles/sass/{abstracts,base,components,layout,pages,themes,vendors}

cd src/styles/sass || exit

################################
# ABSTRACTS
################################

cat <<'EOF' > abstracts/_variables.scss
// VARIABLES
// Store all design tokens here:
// colors, spacing, fonts, z-index, breakpoints, etc.
// This file should NOT output CSS.

$primary-color: #3498db;
$secondary-color: #2ecc71;
$font-main: "Inter", sans-serif;
EOF

cat <<'EOF' > abstracts/_functions.scss
// FUNCTIONS
// Custom Sass functions that return values.
// Example: color manipulation, unit conversions.

@function rem($px) {
  @return $px / 16px * 1rem;
}
EOF

cat <<'EOF' > abstracts/_mixins.scss
// MIXINS
// Reusable style blocks.
// Example: flex centering, responsive breakpoints.

@mixin flex-center {
  display: flex;
  justify-content: center;
  align-items: center;
}
EOF

cat <<'EOF' > abstracts/_placeholders.scss
// PLACEHOLDERS
// %placeholders used with @extend.
// Use for shared base styles.

%card-base {
  border-radius: 12px;
  padding: 1rem;
}
EOF

cat <<'EOF' > abstracts/_index.scss
// ABSTRACTS INDEX
// Re-export all abstract files.

@forward "variables";
@forward "functions";
@forward "mixins";
@forward "placeholders";
EOF

################################
# BASE
################################

cat <<'EOF' > base/_reset.scss
// RESET
// Remove browser inconsistencies.
// Could include a CSS reset or normalize.
*,
*::before,
*::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}
EOF

cat <<'EOF' > base/_typography.scss
// TYPOGRAPHY
// Global text styles.

body {
  font-family: sans-serif;
  line-height: 1.5;
}
EOF

cat <<'EOF' > base/_animations.scss
// ANIMATIONS
// Store keyframes and animation helpers.

@keyframes fade-in {
  from { opacity: 0; }
  to { opacity: 1; }
}
EOF

cat <<'EOF' > base/_utilities.scss
// UTILITIES
// Helper classes like margin/padding utilities.

.u-text-center { text-align: center; }
.u-hidden { display: none; }
EOF

################################
# COMPONENTS
################################

for file in buttons cards forms modals navigation
do
cat <<EOF > components/_$file.scss
// COMPONENT: $file
// Styles for the $file component only.
// Each component should be isolated and reusable.
EOF
done

################################
# LAYOUT
################################

for file in grid header footer sidebar
do
cat <<EOF > layout/_$file.scss
// LAYOUT: $file
// Structural layout styles.
// Used for major page sections.
EOF
done

################################
# PAGES
################################

for file in home about contact
do
cat <<EOF > pages/_$file.scss
// PAGE: $file
// Page-specific styles.
// Avoid heavy styling here; prefer components.
EOF
done

################################
# THEMES
################################

cat <<'EOF' > themes/_dark.scss
// DARK THEME
// Override variables for dark mode.
EOF

cat <<'EOF' > themes/_light.scss
// LIGHT THEME
// Override variables for light mode.
EOF

################################
# VENDORS
################################

cat <<'EOF' > vendors/_normalize.scss
// VENDORS
// Third-party CSS files.
// Example: normalize.css, bootstrap overrides.
EOF

################################
# MAIN ENTRY FILE
################################

cat <<'EOF' > main.scss
// MAIN SCSS ENTRY POINT
// This file loads the entire architecture.

@use "abstracts";
@use "base/reset";
@use "base/typography";
@use "base/animations";
@use "base/utilities";

@use "layout/grid";
@use "layout/header";
@use "layout/footer";
@use "layout/sidebar";

@use "components/buttons";
@use "components/cards";
@use "components/forms";
@use "components/modals";
@use "components/navigation";

@use "pages/home";
@use "pages/about";
@use "pages/contact";
EOF

echo "Sass architecture created successfully."