-- Ensure table exists (JPA usually handles this if ddl-auto is set)
-- Insert test users (with BCrypt-hashed passwords)

INSERT INTO users (username, password, role) VALUES
('agent007', '$2a$10$Q9eV2kh5Dlttc2V4kI9LRe4Ev.5jXRphI6FE/ODG0Z.JPV1eHmt5W', 'USER'), -- password: Test@123
('admin', '$2a$10$ml03ENvpKYJ6AxyqZzlePuxAV6us3MC/mvZ3zSGZcK9Z6gAYU5Dsy', 'ADMIN'), -- password: Admin@123
('testuser', '$2a$10$yT1FC4DRYZDkhw2SPRx5ROyt5BoRwh7PbCS6GzSGKQfppFErln28K', 'USER') -- password: User@123
ON CONFLICT (username) DO NOTHING;

-- src/main/resources/data.sql

-- 5 CRITICAL calamities
INSERT INTO calamities (title, reported, location, description, villain, severity) VALUES
  ('Shock Assault', '5 mins ago', 'Metropolis, Daily Planet Plaza',
   'A massive shockwave ripped through the plaza, sending bystanders scrambling under falling debris.', 'Brainiac', 'CRITICAL'),
  ('City Blackout', '12 mins ago', 'Gotham, Wayne Tower',
   'All power systems in the tower failed simultaneously, plunging corridors into absolute darkness.', 'Doomsday', 'CRITICAL'),
  ('Cataclysmic Breach', '20 mins ago', 'Central City, Particle Accelerator',
   'An enormous tear in reality swallowed lab equipment whole, leaving a crackling void behind.', 'Galactus', 'CRITICAL'),
  ('Quantum Rift', '8 mins ago', 'Star Labs, Cortex Wing',
   'A sudden ripple in the air distorted physics near the core reactor, warping everything around.', 'Darkseid', 'CRITICAL'),
  ('Orbital Crash', '15 mins ago', 'Guardians Base, Orbital Dock',
   'An unidentified vessel slammed into the docking ring, shattering panels across the deck.', 'Thanos', 'CRITICAL')
ON CONFLICT (title) DO NOTHING;

-- 5 HIGH calamities
INSERT INTO calamities (title, reported, location, description, villain, severity) VALUES
  ('Laughing Shadows', '30 mins ago', 'Gotham, Ace Chemicals',
   'A hall of vats filled with noxious gas that induces uncontrollable laughter has breached containment.', 'Joker', 'HIGH'),
  ('Metal Storm', '25 mins ago', 'New York, Stark Industries',
   'Every piece of metal in the facility has been drawn to a pulsating core, fusing doors shut with furnace heat.', 'Magneto', 'HIGH'),
  ('Machine Revolt', '18 mins ago', 'San Francisco, Pym Labs',
   'Automated defense turrets have turned hostile, targeting anyone approaching the main entrance.', 'Ultron', 'HIGH'),
  ('Silent Sentinel', '22 mins ago', 'Wakanda Embassy, Tech Wing',
   'Energy shields are cycling on and off, trapping personnel inside and cutting off communications.', 'Black Panther', 'HIGH'),
  ('Black Tide', '35 mins ago', 'Oscorp R&D Center',
   'A dark, viscous fluid has flooded the labs, corroding surfaces in seconds and whispering through vents.', 'Venom', 'HIGH')
ON CONFLICT (title) DO NOTHING;

-- 5 MEDIUM calamities
INSERT INTO calamities (title, reported, location, description, villain, severity) VALUES
  ('Mind Maze', '40 mins ago', 'Star City, Queen Consolidated',
   'Encrypted puzzles have overridden the security network, locking down vital systems without warning.', 'Riddler', 'MEDIUM'),
  ('Pumpkin Storm', '45 mins ago', 'Queens, Oscorp Tower',
   'Shattered glass and unspent ordnance litter the penthouse suite after an unseen barrage.', 'Green Goblin', 'MEDIUM'),
  ('Steel Snare', '50 mins ago', 'Empire City, Dockyards',
   'Heavy-duty cranes have tangled their own cables into deadly nooses across the loading docks.', 'Doctor Octopus', 'MEDIUM'),
  ('Tidal Surge', '55 mins ago', 'Atlantis Outpost, Reef Lab',
   'A sudden wall of water breached the outer barrier, flooding experiments and shorting circuits.', 'Black Manta', 'MEDIUM'),
  ('High Stakes', '60 mins ago', 'Hell’s Kitchen, Devil’s Den',
   'Illicit gaming tables have activated hidden traps, ensnaring participants under the floor panels.', 'Kingpin', 'MEDIUM')
ON CONFLICT (title) DO NOTHING;

-- 5 LOW calamities
INSERT INTO calamities (title, reported, location, description, villain, severity) VALUES
  ('Whispered Fear', '65 mins ago', 'Coast City, Ferris Dynamics',
   'Employees report sudden waves of panic near the main power conduits, despite no visible threat.', 'Sinestro', 'LOW'),
  ('Ledger Vanish', '70 mins ago', 'Gotham, Wayne Manor',
   'Ancient account books have disappeared overnight, leaving only cryptic ledger entries behind.', 'Bane', 'LOW'),
  ('Radiant Flicker', '75 mins ago', 'Harlem, Banner Labs',
   'Gamma sensors spiked and dropped in seconds, as if something hulking brushed past the detectors.', 'Abomination', 'LOW'),
  ('Rail Jolt', '80 mins ago', 'Queens, Subway Station',
   'Platform rails emitted unexpected high-voltage arcs, startling commuters but causing no lasting harm.', 'Shocker', 'LOW'),
  ('Tea Whistle', '85 mins ago', 'London, Clock Tower',
   'Steam valves in the control room hissed in a rhythmic pattern, unsettling the on-duty technicians.', 'Mad Hatter', 'LOW')
ON CONFLICT (title) DO NOTHING;

