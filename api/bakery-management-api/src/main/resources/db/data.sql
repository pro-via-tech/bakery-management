-- Units
INSERT INTO units (name, display_name, conversion_to_gram, type, is_discrete_unit) VALUES
-- INGREDIENT UNITS
('GRAM', 'g', 1, 'INGREDIENT', false),
('KILOGRAM', 'kg', 1000, 'INGREDIENT', false),
('MILLIGRAM', 'mg', 0.001, 'INGREDIENT', false),
('POUND', 'lb', 453.60, 'INGREDIENT', false),
('OUNCE', 'oz', 28.35, 'INGREDIENT', false),
('LITER', 'L', 1000, 'INGREDIENT', false),
('MILLILITER', 'mL', 1, 'INGREDIENT', false),
('GALLON', 'gal', 3785.41, 'INGREDIENT', false),
('FLUID_OUNCE', 'fl oz', 28.35, 'INGREDIENT', false),
('TEASPOON', 'tsp', 4.2, 'INGREDIENT', false),
('TABLESPOON', 'tbsp', 14.3, 'INGREDIENT', false),
('CUP', 'cup', 240, 'INGREDIENT', false),
('BAG', 'bag', 1000, 'INGREDIENT', true),
('BOTTLE', 'bottle', 500, 'INGREDIENT', true),
-- RECIPE UNITS
('PIECE', 'piece', 1, 'RECIPE', true),
('SLICE', 'slice', 1, 'RECIPE', true),
('DOZEN', 'dozen', 12, 'RECIPE', true),
('LOAF', 'loaf', 1, 'RECIPE', true),
('CAKE', 'cake', 1, 'RECIPE', true),
('MUFFIN', 'muffin', 1, 'RECIPE', true),
('ROLL', 'roll', 1, 'RECIPE', true),
('COOKIE', 'cookie', 1, 'RECIPE', true),
('TART', 'tart', 1, 'RECIPE', true),
('SCONE', 'scone', 1, 'RECIPE', true),
('BUN', 'bun', 1, 'RECIPE', true),
('PACK', 'pack', 1, 'RECIPE', true),
('BATCH', 'batch', 1, 'RECIPE', true),
('TRAY', 'tray', 1, 'RECIPE', true),
-- BOTH
('UNIT', 'unit', 1, 'INGREDIENT', true);


-- Ingredients
INSERT INTO ingredients (
  name, purchase_cost, purchase_quantity, unit_id, supplier, unit_weight_in_grams,
  cost_per_unit, cost_per_gram,
  protein, carbs, fat, calories
) VALUES
('Sourdough Starter', 1.00, 50, (SELECT id FROM units WHERE name = 'GRAM'), 'Self', 1.0, 0.02, 0.0200, 0, 0, 0, 0),
('Organic All Purpose Flour', 21.05, 20, (SELECT id FROM units WHERE name = 'POUND'), 'Costco', 453.6, 1.05, 0.0023, 10, 76, 1, 333),
('Whole Wheat Berries', 16.50, 25, (SELECT id FROM units WHERE name = 'POUND'), 'Azure', 453.6, 0.66, 0.0015, 15, 70, 2, 340),
('Rye Berries', 22.38, 25, (SELECT id FROM units WHERE name = 'POUND'), 'Azure', 453.6, 0.90, 0.0020, 12, 68, 2, 320),
('Semi Sweet Chocolate Chips', 2.29, 1, (SELECT id FROM units WHERE name = 'POUND'), 'Aldi', 453.6, 3.66, 0.0081, 5, 50, 30, 520),
('Cheddar Cheese', 2.50, 1, (SELECT id FROM units WHERE name = 'POUND'), 'Aldi', 453.6, 5.00, 0.0110, 25, 2, 33, 400),
('Grass Fed Butter', 11.69, 2, (SELECT id FROM units WHERE name = 'POUND'), 'Costco', 453.6, 5.85, 0.0129, 1, 0, 81, 729),
('Honey', 14.73, 2, (SELECT id FROM units WHERE name = 'POUND'), 'Costco', 453.6, 9.82, 0.0216, 0, 82, 0, 304),
('Avocado Oil', 25.73, 2, (SELECT id FROM units WHERE name = 'LITER'), 'Costco', 914.0, 12.87, 0.0141, 0, 0, 100, 9000),
('Organic Cane Sugar', 10.52, 10, (SELECT id FROM units WHERE name = 'POUND'), 'Costco', 453.6, 1.05, 0.0023, 0, 100, 0, 400),
('Organic Brown Sugar', 3.09, 1, (SELECT id FROM units WHERE name = 'POUND'), 'Aldi', 453.6, 3.09, 0.0068, 0, 100, 0, 400),
('Sea Salt', 5.37, 5, (SELECT id FROM units WHERE name = 'POUND'), 'Azure', 453.6, 1.07, 0.0024, 0, 0, 0, 0),
('Baking Soda', 3.26, 1, (SELECT id FROM units WHERE name = 'POUND'), 'Azure', 453.6, 3.26, 0.0072, 0, 0, 0, 0),
('Baking Powder', 6.02, 1, (SELECT id FROM units WHERE name = 'POUND'), 'Azure', 453.6, 6.02, 0.0133, 0, 28, 0, 112),
('Eggs', 8.49, 24, (SELECT id FROM units WHERE name = 'UNIT'), 'Costco', 50.0, 0.35, 0.0071, 13, 1, 11, 155),
('Milk', 15.90, 2, (SELECT id FROM units WHERE name = 'GALLON'), 'Costco', 3785.41, 10.60, 0.0028, 3, 5, 4, 60),
('Coconut Oil', 15.20, 84, (SELECT id FROM units WHERE name = 'FLUID_OUNCE'), 'Costco', 28.35, 0.18, 0.0064, 0, 0, 100, 9000),
('Vanilla Extract', 34.00, 750, (SELECT id FROM units WHERE name = 'MILLILITER'), 'Self', 0.95, 0.05, 0.0477, 0, 1, 0, 12),
('Heavy Cream', 4.68, 32, (SELECT id FROM units WHERE name = 'FLUID_OUNCE'), 'Sams', 946.0, 0.15, 0.0002, 2, 3, 35, 340),
('Granulated Sugar', 11.10, 25, (SELECT id FROM units WHERE name = 'POUND'), 'Costco', 453.6, 0.44, 0.0010, 0, 100, 0, 400),
('Frozen Blueberries', 9.82, 3, (SELECT id FROM units WHERE name = 'POUND'), 'Costco', 453.6, 3.27, 0.0072, 1, 14, 0, 57),
('Powdered Sugar', 2.91, 7, (SELECT id FROM units WHERE name = 'POUND'), 'Costco', 453.6, 0.42, 0.0009, 0, 99, 0, 389),
('Coconut Sugar', 10.79, 1.8, (SELECT id FROM units WHERE name = 'KILOGRAM'), 'Costco', 1000, 0.60, 0.0108, 0, 100, 0, 375),
('Cream Cheese', 2.00, 226, (SELECT id FROM units WHERE name = 'GRAM'), 'Costco', 226.0, 0.01, 0.0000, 6, 3, 33, 350),
('Cinnamon', 6.65, 1, (SELECT id FROM units WHERE name = 'POUND'), 'Azure', 453.6, 6.65, 0.0147, 4, 80, 1, 380),
('Water', 0.00, 1, (SELECT id FROM units WHERE name = 'GRAM'), 'Self', 1.0, 0.00, 0.0000, 0, 0, 0, 0);

-- Recipes
INSERT INTO recipes (name, yield_amount, unit_id) VALUES
('Basic Sourdough Starter', 500.0, (SELECT id FROM units WHERE name = 'GRAM')),
('Whole Wheat Bread Dough', 1000.0, (SELECT id FROM units WHERE name = 'GRAM')),
('Rye Bread Dough', 900.0, (SELECT id FROM units WHERE name = 'GRAM')),
('Chocolate Chip Cookies', 1200.0, (SELECT id FROM units WHERE name = 'GRAM')),
('Cream Cheese Frosting', 500.0, (SELECT id FROM units WHERE name = 'GRAM')),
('Cream Cheese Icing', 200.0, (SELECT id FROM units WHERE name = 'GRAM')),
('Cinnamon Buns', 12, (SELECT id FROM units WHERE name = 'UNIT'));

-- Recipe Components
-- 1. Basic Sourdough Starter
INSERT INTO recipe_components (recipe_id, ingredient_id, quantity_used) VALUES
((SELECT id FROM recipes WHERE name = 'Basic Sourdough Starter'), (SELECT id FROM ingredients WHERE name = 'Sourdough Starter'), 500.0),
((SELECT id FROM recipes WHERE name = 'Basic Sourdough Starter'), (SELECT id FROM ingredients WHERE name = 'Organic All Purpose Flour'), 250.0),
((SELECT id FROM recipes WHERE name = 'Basic Sourdough Starter'), (SELECT id FROM ingredients WHERE name = 'Water'), 250.0);

-- 2. Whole Wheat Bread Dough
INSERT INTO recipe_components (recipe_id, ingredient_id, quantity_used) VALUES
((SELECT id FROM recipes WHERE name = 'Whole Wheat Bread Dough'), (SELECT id FROM ingredients WHERE name = 'Whole Wheat Berries'), 600.0),
((SELECT id FROM recipes WHERE name = 'Whole Wheat Bread Dough'), (SELECT id FROM ingredients WHERE name = 'Sea Salt'), 12.0),
((SELECT id FROM recipes WHERE name = 'Whole Wheat Bread Dough'), (SELECT id FROM ingredients WHERE name = 'Honey'), 30.0),
((SELECT id FROM recipes WHERE name = 'Whole Wheat Bread Dough'), (SELECT id FROM ingredients WHERE name = 'Sourdough Starter'), 150.0);

-- 3. Rye Bread Dough
INSERT INTO recipe_components (recipe_id, ingredient_id, quantity_used) VALUES
((SELECT id FROM recipes WHERE name = 'Rye Bread Dough'), (SELECT id FROM ingredients WHERE name = 'Rye Berries'), 650.0),
((SELECT id FROM recipes WHERE name = 'Rye Bread Dough'), (SELECT id FROM ingredients WHERE name = 'Sea Salt'), 10.0),
((SELECT id FROM recipes WHERE name = 'Rye Bread Dough'), (SELECT id FROM ingredients WHERE name = 'Sourdough Starter'), 120.0);

-- 4. Chocolate Chip Cookies
INSERT INTO recipe_components (recipe_id, ingredient_id, quantity_used) VALUES
((SELECT id FROM recipes WHERE name = 'Chocolate Chip Cookies'), (SELECT id FROM ingredients WHERE name = 'Organic All Purpose Flour'), 500.0),
((SELECT id FROM recipes WHERE name = 'Chocolate Chip Cookies'), (SELECT id FROM ingredients WHERE name = 'Semi Sweet Chocolate Chips'), 300.0),
((SELECT id FROM recipes WHERE name = 'Chocolate Chip Cookies'), (SELECT id FROM ingredients WHERE name = 'Grass Fed Butter'), 200.0),
((SELECT id FROM recipes WHERE name = 'Chocolate Chip Cookies'), (SELECT id FROM ingredients WHERE name = 'Eggs'), 100.0),
((SELECT id FROM recipes WHERE name = 'Chocolate Chip Cookies'), (SELECT id FROM ingredients WHERE name = 'Organic Cane Sugar'), 150.0),
((SELECT id FROM recipes WHERE name = 'Chocolate Chip Cookies'), (SELECT id FROM ingredients WHERE name = 'Baking Soda'), 5.0);

-- 5. Cream Cheese Frosting
INSERT INTO recipe_components (recipe_id, ingredient_id, quantity_used) VALUES
((SELECT id FROM recipes WHERE name = 'Cream Cheese Frosting'), (SELECT id FROM ingredients WHERE name = 'Cream Cheese'), 300.0),
((SELECT id FROM recipes WHERE name = 'Cream Cheese Frosting'), (SELECT id FROM ingredients WHERE name = 'Heavy Cream'), 150.0),
((SELECT id FROM recipes WHERE name = 'Cream Cheese Frosting'), (SELECT id FROM ingredients WHERE name = 'Vanilla Extract'), 50.0),
((SELECT id FROM recipes WHERE name = 'Cream Cheese Frosting'), (SELECT id FROM ingredients WHERE name = 'Organic Brown Sugar'), 100.0);

-- 6. Cream Cheese Icing
INSERT INTO recipe_components (recipe_id, ingredient_id, quantity_used) VALUES
((SELECT id FROM recipes WHERE name = 'Cream Cheese Icing'), (SELECT id FROM ingredients WHERE name = 'Cream Cheese'), 100.0),
((SELECT id FROM recipes WHERE name = 'Cream Cheese Icing'), (SELECT id FROM ingredients WHERE name = 'Powdered Sugar'), 70.0),
((SELECT id FROM recipes WHERE name = 'Cream Cheese Icing'), (SELECT id FROM ingredients WHERE name = 'Vanilla Extract'), 10.0),
((SELECT id FROM recipes WHERE name = 'Cream Cheese Icing'), (SELECT id FROM ingredients WHERE name = 'Heavy Cream'), 20.0);

-- 7. Cinnamon Buns - Raw ingredients
INSERT INTO recipe_components (recipe_id, ingredient_id, quantity_used) VALUES
((SELECT id FROM recipes WHERE name = 'Cinnamon Buns'), (SELECT id FROM ingredients WHERE name = 'Organic All Purpose Flour'), 600.0),
((SELECT id FROM recipes WHERE name = 'Cinnamon Buns'), (SELECT id FROM ingredients WHERE name = 'Eggs'), 100.0),
((SELECT id FROM recipes WHERE name = 'Cinnamon Buns'), (SELECT id FROM ingredients WHERE name = 'Milk'), 200.0),
((SELECT id FROM recipes WHERE name = 'Cinnamon Buns'), (SELECT id FROM ingredients WHERE name = 'Organic Cane Sugar'), 100.0),
((SELECT id FROM recipes WHERE name = 'Cinnamon Buns'), (SELECT id FROM ingredients WHERE name = 'Grass Fed Butter'), 100.0),
((SELECT id FROM recipes WHERE name = 'Cinnamon Buns'), (SELECT id FROM ingredients WHERE name = 'Sea Salt'), 10.0),
((SELECT id FROM recipes WHERE name = 'Cinnamon Buns'), (SELECT id FROM ingredients WHERE name = 'Baking Powder'), 8.0),
((SELECT id FROM recipes WHERE name = 'Cinnamon Buns'), (SELECT id FROM ingredients WHERE name = 'Cinnamon'), 20.0);

-- Sub-recipe reference
INSERT INTO recipe_components (recipe_id, sub_recipe_id, quantity_used) VALUES
((SELECT id FROM recipes WHERE name = 'Cinnamon Buns'), (SELECT id FROM recipes WHERE name = 'Cream Cheese Icing'), 200.0);
