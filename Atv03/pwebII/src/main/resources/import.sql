
-- Inserindo Pacientes
INSERT INTO paciente (nome, telefone) VALUES ('Heloisa', '63999991111');
INSERT INTO paciente (nome, telefone) VALUES ('Tandson', '63988882222');

-- Inserindo Médicos
INSERT INTO medico (nome, crm) VALUES ('Dr. Roberto Fernandes', 'CRM/TO 1234');
INSERT INTO medico (nome, crm) VALUES ('Dra. Maria Oliveira', 'CRM/TO 5678');

-- Inserindo Consultas
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-03-10 14:30:00', 250.00, 'Consulta de Rotina', 1, 1);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-03-11 10:00:00', 300.00, 'Retorno do paciente', 2, 2);