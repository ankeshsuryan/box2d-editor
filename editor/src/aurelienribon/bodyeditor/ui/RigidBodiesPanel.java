package aurelienribon.bodyeditor.ui;

import aurelienribon.bodyeditor.Ctx;
import aurelienribon.bodyeditor.models.RigidBodyModel;
import aurelienribon.ui.components.ImagePreviewPanel;
import aurelienribon.ui.css.Style;
import aurelienribon.utils.notifications.AutoListModel;
import aurelienribon.utils.notifications.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import res.Res;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class RigidBodiesPanel extends javax.swing.JPanel {
    public RigidBodiesPanel() {
        initComponents();

		Style.registerCssClasses(headerPanel, ".headerPanel");

		final AutoListModel<RigidBodyModel> listModel = new AutoListModel<RigidBodyModel>(Ctx.bodies.getModels());
		list.setModel(listModel);
		list.addListSelectionListener(listSelectionListener);
		list.setCellRenderer(listCellRenderer);
		Ctx.bodies.addChangeListener(listModelChangeListener);

		createBtn.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {create();}});
		renameBtn.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {rename();}});
		deleteBtn.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {delete();}});

		createBtn.setEnabled(false);
		renameBtn.setEnabled(false);
		deleteBtn.setEnabled(false);

		Ctx.io.addChangeListener(new ChangeListener() {
			@Override public void propertyChanged(Object source, String propertyName) {
				createBtn.setEnabled(Ctx.io.getProjectFile() != null);
			}
		});
    }

	private void create() {
		RigidBodiesCreationDialog dialog = new RigidBodiesCreationDialog(Ctx.window);
		dialog.setLocationRelativeTo(Ctx.window);
		dialog.pack();
		dialog.setVisible(true);
	}

	private void rename() {
		RigidBodyModel model = Ctx.bodies.getSelectedModel();
		String name = JOptionPane.showInputDialog(Ctx.window, "New name of the new rigid body?", model.getName());

		if (name != null) {
			name = name.trim();

			if (name.equals("")) {
				String msg = "Sorry, you cannot use an empty name.";
				JOptionPane.showMessageDialog(Ctx.window, msg);
				return;
			} else if (Ctx.bodies.getModel(name) != null && Ctx.bodies.getModel(name) != model) {
				String msg = "Sorry, there is already a body with this name.";
				JOptionPane.showMessageDialog(Ctx.window, msg);
				return;
			}

			model.setName(name);
		}
	}

	private void delete() {
		for (Object model : list.getSelectedValuesList()) {
			Ctx.bodies.getModels().remove((RigidBodyModel) model);
		}
	}

	// -------------------------------------------------------------------------
	// ListSelection
	// -------------------------------------------------------------------------

	private final ChangeListener listModelChangeListener = new ChangeListener() {
		@Override
		public void propertyChanged(Object source, String propertyName) {
			RigidBodyModel model = Ctx.bodies.getSelectedModel();
			renameBtn.setEnabled(model != null);
			deleteBtn.setEnabled(model != null);

			list.removeListSelectionListener(listSelectionListener);
			if (model != null) list.setSelectedValue(model, true);
			else list.clearSelection();
			list.addListSelectionListener(listSelectionListener);
		}
	};

	private final ListSelectionListener listSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) return;
			Ctx.bodies.select((RigidBodyModel) list.getSelectedValue());
		}
	};

	// -------------------------------------------------------------------------
	// ListCellRenderer
	// -------------------------------------------------------------------------

	private final ListCellRenderer<RigidBodyModel> listCellRenderer = new ListCellRenderer<RigidBodyModel> () {
		private final JPanel panel = new JPanel(new BorderLayout());
		private final JPanel txtPanel = new JPanel(new BorderLayout());
		private final JLabel nameLabel = new JLabel();
		private final JLabel infoLabel = new JLabel();
		private final ImagePreviewPanel imgPanel = new ImagePreviewPanel();

		{
			txtPanel.setOpaque(false);
			txtPanel.add(nameLabel, BorderLayout.CENTER);
			txtPanel.add(infoLabel, BorderLayout.SOUTH);

			panel.setBorder(new EmptyBorder(5, 10, 5, 10));
			panel.add(txtPanel, BorderLayout.CENTER);
			panel.add(imgPanel, BorderLayout.WEST);
			panel.setBackground(new Color(0xBBC8D8));
			infoLabel.setForeground(new Color(0x555555));

			Font font = nameLabel.getFont();
			nameLabel.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));

			font = infoLabel.getFont();
			infoLabel.setFont(new Font(font.getName(), font.getStyle(), 10));

			imgPanel.setPreferredSize(new Dimension(30, 20));
			imgPanel.setFill(Color.WHITE);
			imgPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}

		@Override
		public Component getListCellRendererComponent(JList list, RigidBodyModel value, int index, boolean isSelected, boolean cellHasFocus) {
			nameLabel.setText(value.getName());

			String imgPath = value.getImagePath();

			if (imgPath != null) {
				txtPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
				imgPanel.setVisible(true);

				if (value.isImagePathValid()) {
					infoLabel.setText(imgPath);
					try {imgPanel.setImage(Ctx.io.getImageFile(imgPath));} catch (IOException ex) {}
				} else {
					infoLabel.setText("[not found] " + imgPath);
					try {imgPanel.setImage(Res.getUrl("gfx/unknown.png"));} catch (IOException ex) {}
				}

			} else {
				infoLabel.setText("No associated image");
				txtPanel.setBorder(null);
				imgPanel.setVisible(false);
			}

			panel.setOpaque(isSelected);
			return panel;
		}
	};

	// -------------------------------------------------------------------------
	// Generated stuff
	// -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new aurelienribon.ui.components.PaintedPanel();
        jToolBar1 = new javax.swing.JToolBar();
        createBtn = new javax.swing.JButton();
        renameBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        createBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/gfx/ic_add.png"))); // NOI18N
        createBtn.setText("New");
        createBtn.setFocusable(false);
        createBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        createBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(createBtn);

        renameBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/gfx/ic_edit.png"))); // NOI18N
        renameBtn.setText("Rename");
        renameBtn.setFocusable(false);
        renameBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        renameBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(renameBtn);

        deleteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/gfx/ic_delete.png"))); // NOI18N
        deleteBtn.setText("Delete");
        deleteBtn.setFocusable(false);
        deleteBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(deleteBtn);

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(headerPanel, java.awt.BorderLayout.NORTH);

        jPanel1.setOpaque(false);

        list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(list);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createBtn;
    private javax.swing.JButton deleteBtn;
    private aurelienribon.ui.components.PaintedPanel headerPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JList list;
    private javax.swing.JButton renameBtn;
    // End of variables declaration//GEN-END:variables
}
