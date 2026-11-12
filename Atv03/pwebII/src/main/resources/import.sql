-- ====================================================================
-- 1. Perfis de Usuário (Roles)
-- ====================================================================
INSERT INTO perfil (id, nome) VALUES (1, 'ADMIN');
INSERT INTO perfil (id, nome) VALUES (2, 'SECRETARIO');
INSERT INTO perfil (id, nome) VALUES (3, 'MEDICO');
INSERT INTO perfil (id, nome) VALUES (4, 'PACIENTE');
INSERT INTO perfil (id, nome) VALUES (5, 'USER');

-- ====================================================================
-- 2. Usuários do Sistema (senhas com BCrypt: 'admin' para o admin, '123' para os demais)
-- ====================================================================
INSERT INTO usuario (id, usuario, senha) VALUES (1, 'admin', '$2a$10$1VrKs.9M7k1Njm/uE9q66OZF1TFwQEkPnfpH4EtjM99wRQ9H4Hurm');
INSERT INTO usuario (id, usuario, senha) VALUES (2, 'secretario', '$2a$10$1EE2KqeBxisk3A4eCsYdruWADKYaETVLWClUteP7.9jMPwcjrgyhC');
INSERT INTO usuario (id, usuario, senha) VALUES (3, 'dr.roberto', '$2a$10$1EE2KqeBxisk3A4eCsYdruWADKYaETVLWClUteP7.9jMPwcjrgyhC');
INSERT INTO usuario (id, usuario, senha) VALUES (4, 'dra.maria', '$2a$10$1EE2KqeBxisk3A4eCsYdruWADKYaETVLWClUteP7.9jMPwcjrgyhC');
INSERT INTO usuario (id, usuario, senha) VALUES (5, 'heloisa', '$2a$10$1EE2KqeBxisk3A4eCsYdruWADKYaETVLWClUteP7.9jMPwcjrgyhC');
INSERT INTO usuario (id, usuario, senha) VALUES (6, 'tandson', '$2a$10$1EE2KqeBxisk3A4eCsYdruWADKYaETVLWClUteP7.9jMPwcjrgyhC');
INSERT INTO usuario (id, usuario, senha) VALUES (7, 'user', '$2a$10$1EE2KqeBxisk3A4eCsYdruWADKYaETVLWClUteP7.9jMPwcjrgyhC');

-- ====================================================================
-- 3. Associação Usuário-Perfil (usuario_perfil)
-- ====================================================================
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (1, 1); -- admin: ADMIN
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (1, 5); -- admin: USER
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (2, 2); -- secretario: SECRETARIO
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (2, 5); -- secretario: USER
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (3, 3); -- dr.roberto: MEDICO
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (3, 5); -- dr.roberto: USER
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (4, 3); -- dra.maria: MEDICO
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (4, 5); -- dra.maria: USER
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (5, 4); -- heloisa: PACIENTE
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (5, 5); -- heloisa: USER
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (6, 4); -- tandson: PACIENTE
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (6, 5); -- tandson: USER
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (7, 4); -- user: PACIENTE
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES (7, 5); -- user: USER

-- ====================================================================
-- 4. Pessoas Associadas aos Usuários
-- ====================================================================
-- Pacientes
INSERT INTO pessoa (id, tipo, email, telefone, nome, cpf, usuario_id) VALUES (1, 'PACIENTE', 'heloisa@gmail.com', '63999991111', 'Heloisa', '111.111.111-11', 5);
INSERT INTO pessoa (id, tipo, email, telefone, nome, cpf, usuario_id) VALUES (2, 'PACIENTE', 'tandson@gmail.com', '63988882222', 'Tandson', '222.222.222-22', 6);

-- Médicos
INSERT INTO pessoa (id, tipo, email, telefone, nome, cpf, crm, usuario_id) VALUES (3, 'MEDICO', 'roberto@clinica.com', '63977773333', 'Dr. Roberto Fernandes', '333.333.333-33', 'CRM/TO 1234', 3);
INSERT INTO pessoa (id, tipo, email, telefone, nome, cpf, crm, usuario_id) VALUES (4, 'MEDICO', 'maria@clinica.com', '63966664444', 'Dra. Maria Oliveira', '444.444.444-44', 'CRM/TO 5678', 4);

-- Secretário(a)
INSERT INTO pessoa (id, tipo, email, telefone, nome, cpf, usuario_id) VALUES (5, 'SECRETARIO', 'secretaria@clinica.com', '63933335555', 'Ana Clara Secretária', '555.555.555-55', 2);

-- Administrador do Sistema
INSERT INTO pessoa (id, tipo, email, telefone, nome, cpf, usuario_id) VALUES (6, 'PF', 'admin@clinica.com', '63911112222', 'Administrador Geral', '000.000.000-00', 1);

-- Pessoas Jurídicas (Parceiros/Convênios)
INSERT INTO pessoa (id, tipo, email, telefone, razao_social, cnpj) VALUES (7, 'PJ', 'clinica@clinicasps.com', '63955556666', 'Clinica Sao Paulo SA', '11.111.111/0001-11');
INSERT INTO pessoa (id, tipo, email, telefone, razao_social, cnpj) VALUES (8, 'PJ', 'hospital@hospitalvida.com', '63944447777', 'Hospital Vida Ltda', '22.222.222/0001-22');

-- ====================================================================
-- 5. Consultas
-- ====================================================================
INSERT INTO consulta (id, data, valor, observacao, paciente_id, medico_id) VALUES (1, '2026-03-10 14:30:00', 250.00, 'Consulta de Rotina', 1, 3);
INSERT INTO consulta (id, data, valor, observacao, paciente_id, medico_id) VALUES (2, '2026-03-11 10:00:00', 300.00, 'Retorno do paciente', 2, 4);

-- ====================================================================
-- 6. Reinício dos Sequences/Identities para novos cadastros
-- ====================================================================
ALTER TABLE perfil ALTER COLUMN id RESTART WITH 100;
ALTER TABLE usuario ALTER COLUMN id RESTART WITH 100;
ALTER TABLE pessoa ALTER COLUMN id RESTART WITH 100;
ALTER TABLE consulta ALTER COLUMN id RESTART WITH 100;
